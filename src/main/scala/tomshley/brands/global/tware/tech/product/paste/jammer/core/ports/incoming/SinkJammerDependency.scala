package tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.*
import akka.stream.scaladsl.{Concat, Flow, Source, StreamConverters}
import akka.util.ByteString
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPortAsync
import tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{JammerByteString, JammerParsedRequest}
import tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerConfigKeys

import scala.concurrent.{ExecutionContext, Future}

sealed trait SinkJammerDependency extends IncomingPortAsync[JammerParsedRequest, Future[JammerByteString]] {
  override def executeAsync(inboundModel: JammerParsedRequest)(implicit ec: ExecutionContext): Future[JammerByteString] = {
    given system: ActorSystem = ActorSystem(JammerConfigKeys.JAMMER_ACTOR_SYSTEM_NAME.toValue)

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
      .map(byteString =>
        JammerByteString(byteString)
      )
  }
}

object SinkJammerDependency extends SinkJammerDependency
