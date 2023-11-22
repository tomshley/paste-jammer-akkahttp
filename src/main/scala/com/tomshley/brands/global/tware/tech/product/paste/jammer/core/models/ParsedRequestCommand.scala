package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{PortInboundModel, Model}
import com.tomshley.brands.global.tware.tech.product.paste.common.models.{PasteModule, PastePart, PasteAssetType}


case class ParsedRequestCommand(
                                 jammerRequest: RequestCommand,
                                 httpAssetType: HTTPAssetType,
                                 parts: Seq[PastePart]
                               ) extends PortInboundModel with Model {

}
