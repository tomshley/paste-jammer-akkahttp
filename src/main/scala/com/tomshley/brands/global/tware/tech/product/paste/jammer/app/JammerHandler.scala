package com.tomshley.brands.global.tware.tech.product.paste.jammer.app

import akka.actor.ActorSystem
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.PathMatchers.LongNumber
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Flow, StreamConverters}
import akka.util.ByteString
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.runmainasfuture.http.routing.AkkaRestHandler
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.JammerRequest
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming.{MatchJammerRequest, ParseJammerRequestMatch, SinkJammerDependency}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.outgoing.JammerCachedOrLoaded
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.{JammerConfigKeys, JammerRequestContentTypes}

import java.io.{File, FileInputStream}
import java.nio.file.{Files, Paths}
import scala.concurrent.Future
import scala.util.{Failure, Success}

enum SupportedPasteAssetTypes:
  case JS, CSS, LESS

trait ModulePrimer[T <: SupportedPasteAssetTypes]

object JammerHandler extends JammerHandler

sealed trait JammerHandler extends AkkaRestHandler with ModulePrimer[SupportedPasteAssetTypes.JS.type] {

  def findFiles(fileFilter: (File) => Boolean = (f) => true)(f: File): List[File] = {
    val ss = f.list()
    val list = if (ss == null) {
      Nil
    } else {
      ss.toList.sorted
    }
    val visible = list.filter(_.charAt(0) != '.')
    val these = visible.map(new File(f, _))
    these.filter(fileFilter) ++ these.filter(_.isDirectory).flatMap(findFiles(fileFilter))
  }

  // /paste/1433279379/jawbone.utils.debounce%2Bv1.0%2Cpaste.cookie%2Bv1.0%2Cpaste.guid%2Bv1.0%2Cpaste.io.formdata%2Bv1.0%2Cpaste.oop%2Bv1.0%2Cpaste.util%2Bv1.0%2Cup.wellness.externals%2Bv1.0%2Cpaste.dom%2Bv1.0%2Cpaste.event%2Bv3.0%2Cpaste.has%2Bv3.0%2Cpaste.io%2Bv1.0%2Cjawbone.ui.responsive%2Bv1.0%2Cup.wellness.modals%2Bv1.0%2Cup.common.confirm_url_link%2Bv1.0.js
  def jammerDev = {
    //    val resources = getClass.getClassLoader.getResource(s"paste/scripts/paste.js")

    //    val resources = getClass.getClassLoader.getResources.andThen(x => x.)
    val moduleDirectoryAbsolutePath = "/Users/sgoggles/Library/Mobile Documents/com~apple~CloudDocs/Downloads/paste/paste-resources/src/paste/scripts"
    val moduleDirectoryBuildAbsolutePath = "/Users/sgoggles/Library/Mobile Documents/com~apple~CloudDocs/Downloads/paste/paste-resources/src/paste/scripts/_paste_build"
    val moduleDirectory = new File(moduleDirectoryAbsolutePath)
    val matchedFiles = findFiles(_.getName endsWith ".js")(moduleDirectory)

    //    val source: Source[ByteString, OutputStream] = StreamConverters.asOutputStream()
    //    val sink: Sink[ByteString, Future[ByteString]] = Sink.fold[ByteString, ByteString](ByteString.empty)(_ ++ _)

    //    val (outputStream, result): (OutputStream, Future[ByteString]) =
    //      source.toMat(sink)(Keep.both).run()

    //    val sink = FileIO.toFile(new File("src/main/resources/prime.txt"))

    //    Source.combine(
    //      matchedFiles.map(file =>
    //          StreamConverters.fromInputStream(() => FileInputStream(file))
    //        )
    //        .map(inputSource =>
    //          inputSource.via(
    //            Flow[ByteString].map(_.map(_.toChar.toByte))
    //          )
    //        ).map(
    //          x =>
    //            x.runWith(
    //              StreamConverters.asOutputStream(() => FileOutputStream())
    //            )
    //        ))

    //    val destinationPath: Path = ???
    //
    //    Source.single(ByteString(
    //        """header1,header2,header3
    //          |1,2,3
    //          |4,5,6""".stripMargin))
    //      .via(CsvParsing.lineScanner())
    //      .runWith(FileIO.toPath(destinationPath))

    //    val moduleDirectoryTree = getFileTree(moduleDirectory)
    //    val matchedFiles = moduleDirectoryTree.map(_.getName)

    //    val moduleDirectoryPath = FileSystems.getDefault.getPath(moduleDirectoryAbsolutePath)
    //    val moduleDirectory = Files.walk(moduleDirectoryPath)
    //    val matchedFiles = moduleDirectory
    //    val resources = getClass.getClassLoader.resources
    //    println(resources)
    //    resources
    //    matchedFiles
  }

  override lazy val routes: Seq[Route] = Seq(jammerGet, jamAll)
  private lazy val jamAll: Route =
    get {
      path(
        "paste2" / LongNumber
      ) { (pasteStamp) =>
        extractExecutionContext { implicit executor =>
          given system: ActorSystem = ActorSystem(JammerConfigKeys.JAMMER_ACTOR_SYSTEM_NAME.toValue)

          val moduleFileAbsolutePath = "/Users/sgoggles/Library/Mobile Documents/com~apple~CloudDocs/Downloads/paste/paste-resources/src/paste/scripts/paste.js"
          val moduleFileBuildAbsolutePath = "/Users/sgoggles/Library/Mobile Documents/com~apple~CloudDocs/Downloads/paste/paste-resources/src/paste/scripts/_paste_build/paste.js"
          val moduleFileBuildAbsolutePathPath = Paths.get(moduleFileBuildAbsolutePath)

          val source = StreamConverters.fromInputStream(() => FileInputStream(moduleFileAbsolutePath))

          val result: Future[IOResult] = source.via {
            Flow[ByteString].map {
              _.map {
                  _.toChar.toByte
                }
                .utf8String.toUpperCase()
            }
          }.map(t => ByteString(t)).runWith {
            Files.createDirectories(moduleFileBuildAbsolutePathPath.getParent)
            FileIO.toPath(moduleFileBuildAbsolutePathPath)
          }

          onComplete(result) {
            case Success(resultValue) => complete(
              HttpResponse(
                entity = HttpEntity(
                  ContentTypes.`text/plain(UTF-8)`,
                  "Success"
                )
              )
            )
            case Failure(exception) => complete(InternalServerError, s"An error occurred: ${exception.getMessage}")
          }
        }
      }
    }
  private lazy val jammerGet: Route =
    get {
      path(
        "paste" /
          LongNumber /
          s".+\\.${JammerRequestContentTypes.values.map(_.toExtension).mkString("|")}$$".r
      ) { (pasteStamp, pastePathWithExt) =>
        extractExecutionContext { implicit executor =>
          lazy val jammerParsedMatch =
            ParseJammerRequestMatch.execute(
              MatchJammerRequest.execute(
                JammerRequest(
                  pasteStamp,
                  pastePathWithExt
                )
              )
            )
          lazy val jammerResponse = JammerCachedOrLoaded.executeAsync(
            SinkJammerDependency.executeAsync(
              jammerParsedMatch
            )
          )
          onComplete(jammerResponse.jammerResponseBody) {
            case Success(jammerResponseValue) => complete(
              HttpResponse(
                entity = HttpEntity(
                  jammerResponse.jammerParsedContentType.toContentType,
                  jammerResponseValue
                )
              )
            )
            case Failure(exception) => complete(InternalServerError, s"An error occurred: ${exception.getMessage}")
          }
        }

      }
    }
}
