package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingModel

case class RequestMatchCommand(
                                jammerRequest: RequestCommand,
                                fileExtension: String,
                                jamPathString: String
                              ) extends IncomingModel
