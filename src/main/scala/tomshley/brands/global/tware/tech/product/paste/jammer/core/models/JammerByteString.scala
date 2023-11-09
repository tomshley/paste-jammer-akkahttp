package tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingModel
import akka.util.ByteString

case class JammerByteString(
                          byteString: ByteString
                        ) extends IncomingModel
