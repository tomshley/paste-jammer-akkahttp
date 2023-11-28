package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{PortInboundModel, PortResultingModel}

import scala.concurrent.Future


case class ResponseBodyEvent(
                              jammerRequest: RequestCommand,
                              jammerParsedHTTPAssetType: HTTPAssetType,
                              jammerResponseBody: String
                            ) extends PortResultingModel
