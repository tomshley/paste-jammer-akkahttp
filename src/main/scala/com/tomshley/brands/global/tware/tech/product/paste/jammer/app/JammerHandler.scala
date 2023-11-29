package com.tomshley.brands.global.tware.tech.product.paste.jammer.app

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.PathMatchers.LongNumber
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.IOResult
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.config.HexagonalConfigKeys
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.runmainasfuture.http.routing.AkkaRestHandler
import com.tomshley.brands.global.tware.tech.product.paste.common.models.*
import com.tomshley.brands.global.tware.tech.product.paste.common.ports.incoming.*
import com.tomshley.brands.global.tware.tech.product.paste.common.ports.outgoing.ManifestCreated
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{HTTPAssetType, RequestCommand}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming.{MatchRequest, ParseJammerRequestMatch}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.outgoing.CachedOrLoaded
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerConfigKeys

import scala.concurrent.ExecutionContext
import scala.language.postfixOps
import scala.util.{Failure, Success}

object JammerHandler extends JammerHandler
sealed trait JammerHandler extends AkkaRestHandler{
  given system: ActorSystem = ActorSystem(JammerConfigKeys.JAMMER_ACTOR_SYSTEM_NAME.toValue)

  override lazy val routes: Seq[Route] = Seq(jammerGetRoute)

  private final lazy val assetBuildDirectories = AssetBuildDirectories()
  private def pasteManifest()(implicit ec: ExecutionContext) = LoadManifest.executeAsync(
    LoadManifestCommand(
      assetBuildDirectories
    )
  )
  private lazy val jammerGetRoute: Route =
    get {
      path(
        "paste" /
          LongNumber /
          s".+\\.${HTTPAssetType.values.map(_.toExtension).mkString("|")}$$".r
      ) { (pasteStamp, pastePathWithExt) =>
        extractExecutionContext { implicit executor => {
          onComplete(
            Source.future(pasteManifest()).via(
              Flow[PasteManifest].map(manifest =>
                CachedOrLoaded.executeAsync(
                  ParseJammerRequestMatch.execute(
                    MatchRequest.execute(
                      RequestCommand(
                        pasteStamp,
                        pastePathWithExt,
                        manifest
                      )
                    )
                  )
                )
              )
            ).toMat(
              Sink.head
            )(
              Keep.right
            ).run().flatMap(
              responseBodyEvent => responseBodyEvent)
          ) {
            case Success(resultValue) => complete(
              HttpResponse(
                entity = HttpEntity(
                  resultValue.jammerParsedHTTPAssetType.toContentType,
                  resultValue.jammerResponseBody
                )
              )
            )
            case Failure(exception) => complete(InternalServerError, s"An error occurred: ${exception.getMessage}")
          }
        }
        }
      }
    }
}