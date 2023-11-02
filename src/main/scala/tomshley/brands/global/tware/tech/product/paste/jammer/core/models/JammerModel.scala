package tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.Model
import tomshley.brands.global.tware.tech.product.paste.common.models.ContentTypes
import tomshley.brands.global.tware.tech.product.paste.common.models.DependencyModel

case class JammerModel(
                        dependencies: Seq[DependencyModel],
                        requestPath: String,
                        contentType: ContentTypes ,
                        forceRequireDependencies: Boolean = false
                      ) extends Model
