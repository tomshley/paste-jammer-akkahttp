package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import com.tomshley.brands.global.tware.tech.product.paste.common.models.{DependencyModel, DependencySourceModel}
//import com.yahoo.platform.yui.*
sealed trait CompressDependency extends IncomingPort[DependencySourceModel, DependencyModel] {

}
