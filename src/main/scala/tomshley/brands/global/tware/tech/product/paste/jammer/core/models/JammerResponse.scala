package tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingModel
import tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerRequestContentTypes

import scala.concurrent.Future


case class JammerResponse(
                           jammerRequest: JammerRequest,
                           jammerParsedContentType: JammerRequestContentTypes,
                           jammerResponseBody: Future[String]
                         ) extends IncomingModel
