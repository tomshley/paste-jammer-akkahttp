package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingModel, Model}
import com.tomshley.brands.global.tware.tech.product.paste.common.models.{PasteModule, PastePart, SupportedPasteAssetType}


case class ParsedRequestCommand(
                          jammerRequest: RequestCommand,
                          httpAssetType: HTTPAssetType,
                          parts: Seq[PastePart]
                        ) extends IncomingModel with Model {

}
