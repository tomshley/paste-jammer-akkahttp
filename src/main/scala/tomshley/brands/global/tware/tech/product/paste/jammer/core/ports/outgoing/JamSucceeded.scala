package tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.outgoing

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.OutgoingPort
import tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{JamSuccessModel, JammerModel}

sealed trait JamSucceeded extends OutgoingPort[JammerModel, JamSuccessModel] {
  override def execute(inboundModel: JammerModel): JamSuccessModel = {
    JamSuccessModel()
  }
}

object JamSucceeded extends JamSucceeded
