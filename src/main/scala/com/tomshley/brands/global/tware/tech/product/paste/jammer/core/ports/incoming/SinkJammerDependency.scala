package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.*
import akka.stream.scaladsl.{Concat, Flow, Source, StreamConverters}
import akka.util.ByteString
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingPort, PortAsyncExecution}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{JammerParsedRequest, JammerSourcedDependencies}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerConfigKeys

import scala.concurrent.ExecutionContext

sealed trait SinkJammerDependency extends IncomingPort[JammerParsedRequest, JammerSourcedDependencies] with PortAsyncExecution[JammerParsedRequest, JammerSourcedDependencies] {
  override def executeAsync(inboundModel: JammerParsedRequest)(implicit ec: ExecutionContext): JammerSourcedDependencies = {
    given system: ActorSystem = ActorSystem(JammerConfigKeys.JAMMER_ACTOR_SYSTEM_NAME.toValue)

    JammerSourcedDependencies(
      inboundModel.jammerRequest,
      inboundModel.supportedContentTypes,
      Source.combine(
        inboundModel.jamParts.map { dependency =>
          getClass.getClassLoader.getResource(s"paste/${inboundModel.supportedContentTypes.toDirectoryName}/" + dependency.name + ".js")
        }.map(pathUrl =>
          StreamConverters.fromInputStream(() => pathUrl.openStream())
        ).map(inputSource =>
          inputSource.via(
            Flow[ByteString].map(_.map(_.toChar.toByte))
          )
        ))(Concat[ByteString]).runReduce(_ ++ _)
    )

  }
}

object SinkJammerDependency extends SinkJammerDependency
