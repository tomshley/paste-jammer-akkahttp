package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{Model, PortInboundModel}
import com.tomshley.brands.global.tware.tech.product.paste.common.models.{PasteAssetType, PasteModule, PastePart}


case class ParsedRequestCommand(
                                 jammerRequest: RequestCommand,
                                 httpAssetType: HTTPAssetType,
                                 parts: Seq[PastePart]
                               ) extends PortInboundModel with Model {

}
