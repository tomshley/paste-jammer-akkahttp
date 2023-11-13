package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{Request, RequestMatch}

sealed trait MatchRequest extends IncomingPort[Request, RequestMatch] {
  override def execute(inboundModel: Request): RequestMatch = {
    lazy val fileExtension = inboundModel.jamPathStringWithExtURLPart.split("\\.").last
    RequestMatch(
      inboundModel,
      fileExtension,
      inboundModel.jamPathStringWithExtURLPart.split(s"\\.${fileExtension}").head
    )
  }
}

object MatchRequest extends MatchRequest
