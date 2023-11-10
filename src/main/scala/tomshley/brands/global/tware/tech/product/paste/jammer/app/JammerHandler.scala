package tomshley.brands.global.tware.tech.product.paste.jammer.app

import akka.actor.ActorSystem
import akka.http.caching.LfuCache
import akka.http.caching.scaladsl.{Cache, CachingSettings}
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.PathMatchers.LongNumber
import akka.http.scaladsl.server.{Directives, Route}
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.runmainasfuture.http.routing.AkkaRestHandler
import tomshley.brands.global.tware.tech.product.paste.common.config.PasteCommonConfigKeys
import tomshley.brands.global.tware.tech.product.paste.jammer.core.models.JammerRequest
import tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming.{JammerRequestMatched, ParseJammerRequestMatch, SinkJammerDependency}
import tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.outgoing.JammerCachedOrLoaded
import tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes

import scala.util.{Failure, Success}


object JammerHandler extends JammerHandler

sealed trait JammerHandler extends AkkaRestHandler {

  // /paste/1433279379/jawbone.utils.debounce%2Bv1.0%2Cpaste.cookie%2Bv1.0%2Cpaste.guid%2Bv1.0%2Cpaste.io.formdata%2Bv1.0%2Cpaste.oop%2Bv1.0%2Cpaste.util%2Bv1.0%2Cup.wellness.externals%2Bv1.0%2Cpaste.dom%2Bv1.0%2Cpaste.event%2Bv3.0%2Cpaste.has%2Bv3.0%2Cpaste.io%2Bv1.0%2Cjawbone.ui.responsive%2Bv1.0%2Cup.wellness.modals%2Bv1.0%2Cup.common.confirm_url_link%2Bv1.0.js

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
              JammerRequestMatched.execute(
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

  override lazy val routes: Seq[Route] = Seq(jammerGet)
}
