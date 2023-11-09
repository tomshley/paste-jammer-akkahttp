package tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingModel
import akka.util.ByteString
import tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes

import scala.concurrent.Future

case class JammerSourcedDependencies(
                                      jammerRequest: JammerRequest,
                                      jammerParsedContentType: JammerRequestContentTypes,
                                      byteStringFuture: Future[ByteString]
                                    ) extends IncomingModel
