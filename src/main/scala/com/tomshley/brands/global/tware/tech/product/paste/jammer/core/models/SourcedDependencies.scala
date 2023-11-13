package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import akka.util.ByteString
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingModel
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes

import scala.concurrent.Future

case class SourcedDependencies(
                                jammerRequest: Request,
                                jammerParsedContentType: JammerRequestContentTypes,
                                byteStringFuture: Future[ByteString]
                              ) extends IncomingModel
