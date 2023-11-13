package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingModel, OutgoingModel}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes

import scala.concurrent.Future


case class Response(
                     jammerRequest: Request,
                     jammerParsedContentType: JammerRequestContentTypes,
                     jammerResponseBody: Future[String]
                   ) extends OutgoingModel
