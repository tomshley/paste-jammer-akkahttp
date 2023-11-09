package tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingModel, Model}
import tomshley.brands.global.tware.tech.product.paste.common.models.DependencyModel
import tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes


case class JammerParsedRequest(
                                supportedContentTypes: JammerRequestContentTypes,
                                jamParts: Seq[DependencyModel]
                              ) extends IncomingModel with Model
