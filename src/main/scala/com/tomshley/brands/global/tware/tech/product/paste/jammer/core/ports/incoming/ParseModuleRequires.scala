package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Framing, Merge, Sink, Source}
import akka.util.ByteString
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{Port, PortAsyncExecution}
import com.tomshley.brands.global.tware.tech.product.paste.common.models
import com.tomshley.brands.global.tware.tech.product.paste.common.models.*
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.FileGatherCommand
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerConfigKeys

import java.nio.file.Paths
import scala.concurrent.{ExecutionContext, Future}


sealed trait BuildTheJamManifest extends Port[FileGatherCommand, Future[Seq[PasteModule]]] with PortAsyncExecution[FileGatherCommand, Future[Seq[PasteModule]]] {
  private lazy val pastedocSink = Sink.seq[PasteModule]

  given system: ActorSystem = ActorSystem(JammerConfigKeys.JAMMER_ACTOR_SYSTEM_NAME.toValue)

  override def executeAsync(inboundModel: FileGatherCommand)(implicit ec: ExecutionContext): Future[Seq[PasteModule]] = {
    lazy val gatheredFiles = inboundModel.absPaths.map(
      Paths.get(_)
    ).map { path =>
      PasteModule(
        part = PastePart(
          path
        ),
        sourceFile = path.toFile
      )
    }
    Source.combine(
      gatheredFiles
        .filter(pasteModule =>
          inboundModel.supportedPasteAssetTypes.contains(pasteModule.part.pasteAssetType)
        ).map(pasteModule =>
          FileIO.fromPath(pasteModule.sourceFile.toPath).via(matchPastDocFlow(pasteModule))
        )
    )(Merge(_)).runWith(pastedocSink)
  }

  private def matchPastDocFlow(pasteModule: PasteModule) =
    Framing.delimiter(ByteString(System.lineSeparator()), maximumFrameLength = 2048, allowTruncation = true)
    .filter { byteString =>
      PastedocExpression.JS_COMMENT.toRegex.findFirstMatchIn(byteString.utf8String).isDefined
    }.map { byteString =>
      val pastedocExpressionMatch = PastedocExpression.JSDOC_MODULE.toRegex.findFirstMatchIn(byteString.utf8String)
      // For later reference:
      //        m foreach {
      //          m => {
      //            println(m.group(0)) // the full match e.g. @requiresParts paste
      //            println(m.group(1)) // the type e.g. requiresParts
      //            println(m.group(2)) // the module name e.g. paste
      //          }
      //        }

      PasteModuleMatch(
        pasteModule,
        PastedocMatch(
          pastedocExpressionMatch.get.group(1), // Note: since we run a qualifier in the previous step, rely on the group being there
          pastedocExpressionMatch.get.group(2) // Note: since we run a qualifier in the previous step, rely on the group being there
        )
      )
    }.fold(Some(pasteModule)) {
      (lastPastModuleOption: Option[PasteModule], nextPasteModuleMatch: PasteModuleMatch) => {
        lazy val requires: Seq[PastePart] = nextPasteModuleMatch.pastedocMatch.pastePartType match
          case PastePartType.REQUIRES => Seq(
            PastePart(
              nextPasteModuleMatch.pastedocMatch.partName,
              pasteModule.part.pasteAssetType
            )
          )
          case _ => Seq()

        lastPastModuleOption match
          case Some(value) =>

            lazy val modulePart = nextPasteModuleMatch.pastedocMatch.pastePartType match
              case PastePartType.MODULE => PastePart(
                nextPasteModuleMatch.pastedocMatch.partName,
                value.part.pasteAssetType,
                value.part.versionOption
              )
              case _ => value.part

            Some(PasteModule(
              modulePart,
              value.sourceFile,
              value.requiresParts ++ requires,
              value.optimizedFileOption,
              value.parentBuildDirOption
            ))
          case None => Option.empty[PasteModule]
      }
    }.filter(
      _.isDefined
    ).map(
      _.get
    )
}

object BuildTheJamManifest extends BuildTheJamManifest
