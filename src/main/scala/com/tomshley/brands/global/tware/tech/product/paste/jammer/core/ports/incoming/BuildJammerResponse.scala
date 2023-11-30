package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingPort, OutgoingPort, Port, PortAsyncExecution}
import com.tomshley.brands.global.tware.tech.product.paste.common.models.{AssetBuildDirectories, LoadManifestCommand, PasteManifest}
import com.tomshley.brands.global.tware.tech.product.paste.common.ports.LoadManifest
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{LoadManifestoRequestCommand, RequestCommand, ResponseBodyEvent}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerConfigKeys

import scala.concurrent.{ExecutionContext, Future}


object BuildJammerResponse extends BuildJammerResponse

trait BuildJammerResponse extends Port[LoadManifestoRequestCommand, Future[ResponseBodyEvent]] with PortAsyncExecution[LoadManifestoRequestCommand, Future[ResponseBodyEvent]] {
  given system: ActorSystem = ActorSystem(JammerConfigKeys.JAMMER_ACTOR_SYSTEM_NAME.toValue)

  override def executeAsync(inboundModel: LoadManifestoRequestCommand)(implicit ec: ExecutionContext): Future[ResponseBodyEvent] = {
    lazy val futureManifest = LoadManifest.executeAsync(LoadManifestCommand(AssetBuildDirectories()))

    Source.future(futureManifest).via(
      Flow[PasteManifest].map(manifest =>
        CacheOrLoad.executeAsync(
          ParseJammerRequestMatch.execute(
            MatchRequest.execute(
              RequestCommand(
                inboundModel.jamBuildStampURLPart,
                inboundModel.jamPathStringWithExtURLPart,
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
      responseBodyEvent => responseBodyEvent
    )
  }
}
