package tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingModel, Model}

case class JammerRequestMatch(
                               jammerRequest: JammerRequest,
                               fileExtension: String,
                               jamPathString: String
                             ) extends IncomingModel
