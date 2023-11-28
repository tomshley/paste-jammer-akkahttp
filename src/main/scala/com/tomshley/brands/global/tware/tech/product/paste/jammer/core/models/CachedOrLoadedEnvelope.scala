package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.PortInboundModel
import com.tomshley.brands.global.tware.tech.product.paste.common.models.SinkDependencyCommand

case class CachedOrLoadedEnvelope(
                                   jammerRequest: RequestCommand,
                                   httpAssetType: HTTPAssetType,
                                   sinkDependencyCommand: SinkDependencyCommand
                                 ) extends PortInboundModel
