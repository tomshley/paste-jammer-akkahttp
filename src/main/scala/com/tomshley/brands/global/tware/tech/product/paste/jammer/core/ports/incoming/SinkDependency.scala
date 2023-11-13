package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.*
import akka.stream.scaladsl.{Concat, Flow, Source, StreamConverters}
import akka.util.ByteString
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingPort, PortAsyncExecution}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{ParsedRequest, SourcedDependencies}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerConfigKeys

import scala.concurrent.ExecutionContext

sealed trait SinkJammerDependency extends IncomingPort[ParsedRequest, SourcedDependencies] with PortAsyncExecution[ParsedRequest, SourcedDependencies] {
  override def executeAsync(inboundModel: ParsedRequest)(implicit ec: ExecutionContext): SourcedDependencies = {
    given system: ActorSystem = ActorSystem(JammerConfigKeys.JAMMER_ACTOR_SYSTEM_NAME.toValue)

    SourcedDependencies(
      inboundModel.jammerRequest,
      inboundModel.supportedContentTypes,
      Source.combine(
        inboundModel.jamParts.map { dependency =>
          getClass.getClassLoader.getResource(s"paste/${inboundModel.supportedContentTypes.toDirectoryName}/" + dependency.name + s".${inboundModel.supportedContentTypes.toExtension}")
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
