package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.google.javascript.*
import com.googlecode.htmlcompressor.*
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.Port
import com.tomshley.brands.global.tware.tech.product.paste.common.models.Module
import com.yahoo.platform.yui.compressor.*

sealed trait CompressDependency extends Port[Module, Module] {
  //  YUICompressor.main()
}
