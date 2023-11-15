package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{RequestCommand, RequestMatchCommand}

sealed trait MatchRequest extends IncomingPort[RequestCommand, RequestMatchCommand] {
  override def execute(inboundModel: RequestCommand): RequestMatchCommand = {
    lazy val fileExtension = inboundModel.jamPathStringWithExtURLPart.split("\\.").last
    RequestMatchCommand(
      inboundModel,
      fileExtension,
      inboundModel.jamPathStringWithExtURLPart.split(s"\\.${fileExtension}").head
    )
  }
}

object MatchRequest extends MatchRequest
