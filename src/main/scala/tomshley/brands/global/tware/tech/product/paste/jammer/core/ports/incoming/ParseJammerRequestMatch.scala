package tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import tomshley.brands.global.tware.tech.product.paste.common.models.DependencyModel
import tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{JammerParsedRequest, JammerRequestMatch}
import tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes

sealed trait ParseJammerRequestMatch extends IncomingPort[JammerRequestMatch, JammerParsedRequest] {
  override def execute(inboundModel: JammerRequestMatch): JammerParsedRequest = {
    JammerParsedRequest(
      inboundModel.jammerRequest,
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
