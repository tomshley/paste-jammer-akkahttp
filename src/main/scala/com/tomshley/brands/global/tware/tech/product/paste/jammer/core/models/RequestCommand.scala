package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.PortInboundModel


case class RequestCommand(
                           jamBuildStampURLPart: Long,
                           jamPathStringWithExtURLPart: String
                         ) extends PortInboundModel
