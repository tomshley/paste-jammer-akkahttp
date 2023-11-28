package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.PortInboundModel
import com.tomshley.brands.global.tware.tech.product.paste.common.models.PasteManifest

case class RequestMatchCommand(
                                jammerRequest: RequestCommand,
                                pasteManifest: PasteManifest,
                                fileExtension: String,
                                jamPathString: String
                              ) extends PortInboundModel
