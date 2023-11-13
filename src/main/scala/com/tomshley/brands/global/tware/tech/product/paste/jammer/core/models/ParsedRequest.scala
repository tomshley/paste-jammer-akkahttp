package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingModel, Model}
import com.tomshley.brands.global.tware.tech.product.paste.common.models.Module
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes


case class ParsedRequest(
                          jammerRequest: Request,
                          supportedContentTypes: JammerRequestContentTypes,
                          jamParts: Seq[Module]
                              ) extends IncomingModel with Model
