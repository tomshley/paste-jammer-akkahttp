package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import com.tomshley.brands.global.tware.tech.product.paste.common.models.{PasteModule, PastePart, SupportedPasteAssetTypes}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{ParsedRequest, RequestMatch}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes

sealed trait ParseJammerRequestMatch extends IncomingPort[RequestMatch, ParsedRequest] {
  override def execute(inboundModel: RequestMatch): ParsedRequest = {
    lazy val contentType = JammerRequestContentTypes.values.find(
      _.toExtension == inboundModel.fileExtension
    ).get
    ParsedRequest(
      inboundModel.jammerRequest,
      contentType,
      inboundModel.jamPathString.split(",").map(s =>
        PastePart(
          s,
          SupportedPasteAssetTypes.values.find(
            _.toFileExtension == inboundModel.fileExtension
          ).get
        )
      )
    )
  }
}

object ParseJammerRequestMatch extends ParseJammerRequestMatch
