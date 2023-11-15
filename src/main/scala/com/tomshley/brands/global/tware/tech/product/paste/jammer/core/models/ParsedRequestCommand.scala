package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingModel, Model}
import com.tomshley.brands.global.tware.tech.product.paste.common.models.{PasteModule, PastePart}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes


case class ParsedRequestCommand(
                          jammerRequest: RequestCommand,
                          supportedContentTypes: JammerRequestContentTypes,
                          jamParts: Seq[PastePart]
                        ) extends IncomingModel with Model
