package tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{JammerRequest, JammerRequestMatch}

sealed trait JammerRequestMatched extends IncomingPort[JammerRequest, JammerRequestMatch]{
  override def execute(inboundModel: JammerRequest): JammerRequestMatch = {
    lazy val fileExtension = inboundModel.jamPathStringWithExtURLPart.split("\\.").last
    JammerRequestMatch(
      inboundModel,
      fileExtension,
      inboundModel.jamPathStringWithExtURLPart.split(s"\\.${fileExtension}").head
    )
  }
}

object JammerRequestMatched extends JammerRequestMatched
