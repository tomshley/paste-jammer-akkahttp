package tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{ExplodeDependenciesModel, JamRequestModel}
import tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{ExplodeDependenciesModel, JamRequestModel}

trait JamRequest extends IncomingPort[JamRequestModel, ExplodeDependenciesModel]{
  override def execute(inboundModel:JamRequestModel): ExplodeDependenciesModel = {
    ExplodeDependenciesModel()
  }
}

object JamRequest extends JamRequest
