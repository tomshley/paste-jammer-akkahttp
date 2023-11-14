package com.tomshley.brands.global.tware.tech.product.paste.jammer.app

import akka.actor.ActorSystem
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.PathMatchers.LongNumber
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.scaladsl.{FileIO, Framing, Merge, Sink, Source}
import akka.util.ByteString
import akka.{Done, NotUsed}
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.runmainasfuture.http.routing.AkkaRestHandler
import com.tomshley.brands.global.tware.tech.product.paste.common.models.*
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{FileGather, Request, ResourceFileDirectories}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming.*
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.outgoing.JammerCachedOrLoaded
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.{JammerConfigKeys, JammerRequestContentTypes}

import java.nio.file.Paths
import scala.concurrent.ExecutionContext
import scala.language.postfixOps
import scala.util.{Failure, Success}


trait ModulePrimer[T <: SupportedPasteAssetTypes]

object JammerHandler extends JammerHandler

sealed trait JammerHandler extends AkkaRestHandler with ModulePrimer[SupportedPasteAssetTypes.JS.type] {
  override lazy val routes: Seq[Route] = Seq(jammerGet, jamAll, jamManifest)
  private final lazy val startingPoint = ResourceFileDirectories()
  private final lazy val gatheredResources = GatherResourceFiles.execute(startingPoint)

  private lazy val jamAll: Route =
    get {
      path(
        "paste2" / LongNumber
      ) { (pasteStamp) =>
        extractExecutionContext { implicit executor =>
          onComplete(EnsureBuildDirectories.executeAsync(startingPoint)) {
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
              MatchRequest.execute(
                Request(
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

      //          val compressorFuture: Future[String] = Future {
      //
      //            //            val errorReporter = new YuiErrorReporter()
      //            //            val in = new InputStreamReader(new FileInputStream(moduleFileAbsolutePath), yuiEncoding)
      //            //            val out2 = new OutputStreamWriter(new FileOutputStream(moduleFileBuildAbsolutePathPath.toFile), yuiEncoding)
      //
      //            lazy val options = new CompilerOptions()
      //
      //            //            CompilerOptions
      //
      //            val compiler = new ClosureCompiler
      //
      //            val result = compiler.compile(
      //              SourceFile.fromCode("fuck", ""),
      //              SourceFile.fromFile(moduleFileAbsolutePath),
      //              options
      //            )
      //
      //            val errors = result.errors
      //            val warnings = result.warnings
      //
      //            //            val compressor = new JavaScriptCompressor(in, errorReporter)
      //            //
      //            //            compressor.compress(out2, -1, true, true, true, true)
      //            //            Files.createDirectories(moduleFileBuildAbsolutePathPath.getParent)
      //
      //            val compilerResult = compiler.toSource
      //
      //            compilerResult
      //          }
      //
      //          val source: Source[String, NotUsed] = Source.future(compressorFuture)
      //          val sink: Sink[String, Future[Done]] = Sink.foreach((s: String) => println(s))
      //
      //          //          val done: Future[Done] = source.runWith(sink) //10
      //          val done: Future[IOResult] = source.map(s => ByteString(s)).runWith {
      //            Files.createDirectories(moduleFileBuildAbsolutePathPath.getParent)
      //            FileIO.toPath(moduleFileBuildAbsolutePathPath)
      //          }

      //          val source = StreamConverters.fromInputStream(() => FileInputStream(moduleFileAbsolutePath))


      //          lazy val source = StreamConverters.fromInputStream(() => FileInputStream(moduleFileAbsolutePath))
      //          lazy val options = new CompilerOptions()


      //          val result: Future[IOResult] = source.via {
      //            Flow[ByteString].map { byteString =>
      //              val codeUTF8 = byteString.map { byte =>
      //                byte.toChar.toByte
      //              }.utf8String
      //
      //
      //
      //              //            CompilerOptions
      //              val sourceFile = SourceFile.fromCode(moduleFileAbsolutePath, codeUTF8, SourceKind.WEAK)
      //
      //              val compiler = new ClosureCompiler
      //
      //              val result = compiler.compile(
      //                SourceFile.fromCode("fuck", ""),
      //                sourceFile,
      //                options
      //              )
      //
      //
      //              val errors = result.errors
      //              val warnings = result.warnings
      //
      //              val compilerResult = compiler.toSource
      //
      //              compilerResult
      //
      //            }
      //          }.map(t => ByteString(t)).runWith {
      //            Files.createDirectories(moduleFileBuildAbsolutePathPath.getParent)
      //            FileIO.toPath(moduleFileBuildAbsolutePathPath)
      //          }
      //          val result: Future[IOResult] = source.via {
      //            Flow[ByteString].map { byteString =>
      //              lazy val yuiContent = byteString.map { byte =>
      //                  byte.toChar.toByte
      //                }.utf8String
      //
      //
      //
      //            }
      //          }
      ////            .via{
      ////            Flow[String].map { str =>
      ////              str.toLowerCase()
      ////            }
      //          }.map(t => ByteString(t)).runWith {
      //            Files.createDirectories(moduleFileBuildAbsolutePathPath.getParent)
      //            FileIO.toPath(moduleFileBuildAbsolutePathPath)
      //          }

    }

  private lazy val jamManifest: Route =
    get {
      path(
        "paste3" / LongNumber
      ) { (pasteStamp) =>
        extractExecutionContext { implicit executor =>
          //          onComplete(coalesce()) {
          onComplete(BuildTheJamManifest.executeAsync(gatheredResources)) {
            case Success(resultValue) => complete(
              HttpResponse(
                entity = HttpEntity(
                  ContentTypes.`text/plain(UTF-8)`,
                  resultValue.toString()
                )
              )
            )
            case Failure(exception) => complete(InternalServerError, s"An error occurred: ${exception.getMessage}")
          }
        }
      }
    }

}
