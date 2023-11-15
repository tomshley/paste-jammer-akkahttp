package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import com.tomshley.brands.global.tware.tech.product.paste.common.models.{PastePart, SupportedPasteAssetType}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{ParsedRequestCommand, RequestMatchCommand}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes

sealed trait ParseJammerRequestMatch extends IncomingPort[RequestMatchCommand, ParsedRequestCommand] {
  override def execute(inboundModel: RequestMatchCommand): ParsedRequestCommand = {
    lazy val contentType = JammerRequestContentTypes.values.find(
      _.toExtension == inboundModel.fileExtension
    ).get
    ParsedRequestCommand(
      inboundModel.jammerRequest,
      contentType,
      inboundModel.jamPathString.split(",").map(s =>
        PastePart(
          s,
          SupportedPasteAssetType.values.find(
            _.toFileExtension == inboundModel.fileExtension
          ).get
        )
      )
    )
  }
}

object ParseJammerRequestMatch extends ParseJammerRequestMatch
