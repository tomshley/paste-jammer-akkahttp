package tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import tomshley.brands.global.tware.tech.product.paste.jammer.core.models.ExplodeDependenciesModel
import tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{ExplodeDependenciesModel, JamRequestModel, JammerModel}

sealed trait ExplodeDependencies extends IncomingPort[ExplodeDependenciesModel, ExplodeDependenciesModel] {
  override def execute(inboundModel: ExplodeDependenciesModel): ExplodeDependenciesModel = {
    ExplodeDependenciesModel()
  }
}

object ExplodeDependencies extends ExplodeDependencies
