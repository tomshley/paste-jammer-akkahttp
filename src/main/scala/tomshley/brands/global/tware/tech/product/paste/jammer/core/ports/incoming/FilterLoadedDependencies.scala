package tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import tomshley.brands.global.tware.tech.product.paste.common.models.ContentTypes
import tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{ExplodeDependenciesModel, JammerModel}

sealed trait FilterLoadedDependencies extends IncomingPort[ExplodeDependenciesModel, JammerModel] {
  override def execute(inboundModel: ExplodeDependenciesModel): JammerModel = {
    JammerModel(
      dependencies=Seq(),
      requestPath="foo",
      contentType=ContentTypes.JS,
      forceRequireDependencies = false
    )
  }
}

object FilterLoadedDependencies extends FilterLoadedDependencies
