package tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import tomshley.brands.global.tware.tech.product.paste.jammer.core.models.PrimerModel

sealed trait BuildManifest extends IncomingPort[PrimerModel, PrimerModel]{
  override def execute(inboundModel: PrimerModel): PrimerModel = {
    PrimerModel()
  }
}

object BuildManifest extends BuildManifest
