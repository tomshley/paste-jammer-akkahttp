package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import com.tomshley.brands.global.tware.tech.product.paste.common.models.Module
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{ParsedRequest, RequestMatch}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes

sealed trait ParseJammerRequestMatch extends IncomingPort[RequestMatch, ParsedRequest] {
  override def execute(inboundModel: RequestMatch): ParsedRequest = {
    ParsedRequest(
      inboundModel.jammerRequest,
      JammerRequestContentTypes.values.find(
        _.toExtension == inboundModel.fileExtension
      ).get,
      inboundModel.jamPathString.split(",").map(s =>
        Module(
          s,
          0.0,
          Seq()
        )
      )
    )
  }
}

object ParseJammerRequestMatch extends ParseJammerRequestMatch
