package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingModel, OutgoingModel}

import scala.concurrent.Future


case class ResponseBodyEvent(
                                       jammerRequest: RequestCommand,
                                       jammerParsedHTTPAssetType: HTTPAssetType,
                                       jammerResponseBody: Future[String]
                   ) extends OutgoingModel
