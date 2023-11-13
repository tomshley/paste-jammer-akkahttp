package com.tomshley.brands.global.tware.tech.product.paste.jammer.app

import akka.Done
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.PathMatchers.LongNumber
import akka.http.scaladsl.server.{Directives, Route}
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.runmainasfuture.http.routing.AkkaRestHandler
import com.tomshley.brands.global.tware.tech.product.paste.common.models.SupportedPasteAssetTypes
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{Request, ResourceFileDirectories}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming.*
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.outgoing.JammerCachedOrLoaded
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes

import scala.language.postfixOps
import scala.util.{Failure, Success}


trait ModulePrimer[T <: SupportedPasteAssetTypes]

object JammerHandler extends JammerHandler

sealed trait JammerHandler extends AkkaRestHandler with ModulePrimer[SupportedPasteAssetTypes.JS.type] {


  // /paste/1433279379/jawbone.utils.debounce%2Bv1.0%2Cpaste.cookie%2Bv1.0%2Cpaste.guid%2Bv1.0%2Cpaste.io.formdata%2Bv1.0%2Cpaste.oop%2Bv1.0%2Cpaste.util%2Bv1.0%2Cup.wellness.externals%2Bv1.0%2Cpaste.dom%2Bv1.0%2Cpaste.event%2Bv3.0%2Cpaste.has%2Bv3.0%2Cpaste.io%2Bv1.0%2Cjawbone.ui.responsive%2Bv1.0%2Cup.wellness.modals%2Bv1.0%2Cup.common.confirm_url_link%2Bv1.0.js
  //  def jammerDev = {
  //    //    val resources = getClass.getClassLoader.getResource(s"paste/scripts/paste.js")
  //
  //    //    val resources = getClass.getClassLoader.getResources.andThen(x => x.)
  //    //    val moduleDirectoryAbsolutePath = "/Users/sgoggles/Library/Mobile Documents/com~apple~CloudDocs/Downloads/paste/paste-resources/src/paste/scripts"
  //    //    val moduleDirectoryBuildAbsolutePath = "/Users/sgoggles/Library/Mobile Documents/com~apple~CloudDocs/Downloads/paste/paste-resources/src/paste/scripts/_paste_build"
  //    //    val moduleDirectory = new File(moduleDirectoryAbsolutePath)
  //    //    val matchedFiles = findFiles(_.getName endsWith ".js")(moduleDirectory)
  //
  //    //    val source: Source[ByteString, OutputStream] = StreamConverters.asOutputStream()
  //    //    val sink: Sink[ByteString, Future[ByteString]] = Sink.fold[ByteString, ByteString](ByteString.empty)(_ ++ _)
  //
  //    //    val (outputStream, result): (OutputStream, Future[ByteString]) =
  //    //      source.toMat(sink)(Keep.both).run()
  //
  //    //    val sink = FileIO.toFile(new File("src/main/resources/prime.txt"))
  //
  //    //    Source.combine(
  //    //      matchedFiles.map(file =>
  //    //          StreamConverters.fromInputStream(() => FileInputStream(file))
  //    //        )
  //    //        .map(inputSource =>
  //    //          inputSource.via(
  //    //            Flow[ByteString].map(_.map(_.toChar.toByte))
  //    //          )
  //    //        ).map(
  //    //          x =>
  //    //            x.runWith(
  //    //              StreamConverters.asOutputStream(() => FileOutputStream())
  //    //            )
  //    //        ))
  //
  //    //    val destinationPath: Path = ???
  //    //
  //    //    Source.single(ByteString(
  //    //        """header1,header2,header3
  //    //          |1,2,3
  //    //          |4,5,6""".stripMargin))
  //    //      .via(CsvParsing.lineScanner())
  //    //      .runWith(FileIO.toPath(destinationPath))
  //
  //    //    val moduleDirectoryTree = getFileTree(moduleDirectory)
  //    //    val matchedFiles = moduleDirectoryTree.map(_.getName)
  //
  //    //    val moduleDirectoryPath = FileSystems.getDefault.getPath(moduleDirectoryAbsolutePath)
  //    //    val moduleDirectory = Files.walk(moduleDirectoryPath)
  //    //    val matchedFiles = moduleDirectory
  //    //    val resources = getClass.getClassLoader.resources
  //    //    println(resources)
  //    //    resources
  //    //    matchedFiles
  //  }

  //  class YUIOptions {
  //    var charset = "UTF-8"
  //    var lineBreakPos: Int = -1
  //    var munge = true
  //    var verbose = false
  //    var preserveAllSemiColons = false
  //    var disableOptimizations = false
  //  }

  //  class YuiErrorReporter extends ErrorReporter with SLogger {
  //    def logit(level: Any, message: String, sourceName: String, line: Int, lineSource: String, lineOffset: Int) = {
  //      logger.debug(message)
  //      //      log.log(level, "%s in %s:%d,%d at %s".format(message, sourceName, line, lineOffset, lineSource))
  //    }
  //
  //    def error(message: String, sourceName: String, line: Int, lineSource: String, lineOffset: Int) = {
  //      logger.error(message)
  //      //      logit(Level.Error, message, sourceName, line, lineSource, lineOffset)
  //    }
  //
  //    def runtimeError(message: String, sourceName: String, line: Int, lineSource: String, lineOffset: Int): EvaluatorException = {
  //      error(message, sourceName, line, lineSource, lineOffset)
  //      new EvaluatorException(message, sourceName, line, lineSource, lineOffset)
  //    }
  //
  //    def warning(message: String, sourceName: String, line: Int, lineSource: String, lineOffset: Int) = {
  //      logger.warn(message)
  //      //      logit(Level.Warn, message, sourceName, line, lineSource, lineOffset)
  //    }
  //
  //  }

  override lazy val routes: Seq[Route] = Seq(jammerGet, jamAll)
  private lazy val jamAll: Route =
    get {
      path(
        "paste2" / LongNumber
      ) { (pasteStamp) =>
        extractExecutionContext { implicit executor =>
          onComplete(EnsureBuildDirectories.executeAsync(ResourceFileDirectories())) {
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
