package tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming


import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{JammerParsedRequest, JammerRequestMatch}
import tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes

import scala.concurrent.Future
import scala.util.{Failure, Success}
import akka.actor.ActorSystem
import akka.http.scaladsl.model.*
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.PathMatchers.LongNumber
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.scaladsl.{Concat, Flow, Source, StreamConverters}
import akka.util.ByteString
import tomshley.brands.global.tware.tech.product.paste.common.models.DependencyModel
sealed trait ParseJammerRequestMatch extends IncomingPort[JammerRequestMatch, JammerParsedRequest]{
  override def execute(inboundModel: JammerRequestMatch): JammerParsedRequest = {
    JammerParsedRequest(
      JammerRequestContentTypes.values.find(
        _.toExtension == inboundModel.fileExtension
      ).get,
      inboundModel.jamPathString.split(",").map(s =>
        DependencyModel(
          s,
          0.0
        )
      )
    )
  }
}

object ParseJammerRequestMatch extends ParseJammerRequestMatch
