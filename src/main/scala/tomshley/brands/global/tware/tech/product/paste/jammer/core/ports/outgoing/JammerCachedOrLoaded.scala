package tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.outgoing

import akka.actor.ActorSystem
import akka.http.caching.LfuCache
import akka.http.caching.scaladsl.{Cache, CachingSettings}
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingPort, IncomingPortAsync, Port, PortAsyncExecution}
import tomshley.brands.global.tware.tech.product.paste.common.config.PasteCommonConfigKeys
import tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{JammerParsedRequest, JammerRequest, JammerResponse, JammerSourcedDependencies}

import scala.concurrent.{ExecutionContext, Future}

sealed trait JammerCachedOrLoaded extends IncomingPort[JammerSourcedDependencies, JammerResponse] with PortAsyncExecution[JammerSourcedDependencies, JammerResponse] {
  given system: ActorSystem = ActorSystem(PasteCommonConfigKeys.HTTP_LFU_CACHE_NAME.toValue)

  private lazy val defaultCachingSettings = CachingSettings(system)
  private lazy val lfuCacheSettings = defaultCachingSettings.lfuCacheSettings
    .withInitialCapacity(PasteCommonConfigKeys.HTTP_LFU_CACHE_INITIAL_SIZE.toValue.toInt)
    .withMaxCapacity(PasteCommonConfigKeys.HTTP_LFU_CACHE_MAX_CAPACITY.toValue.toInt)

  private lazy val cachingSettings =
    defaultCachingSettings.withLfuCacheSettings(lfuCacheSettings)

  private lazy val cachePaste: Cache[String, String] = LfuCache(cachingSettings)

  override def executeAsync(inboundModel: JammerSourcedDependencies)(implicit ec: ExecutionContext): JammerResponse = {
    JammerResponse(
      inboundModel.jammerRequest,
      inboundModel.jammerParsedContentType,
      cachePaste.getOrLoad(
        "ofo",
        _ => {
          inboundModel.byteStringFuture.map(
            _.utf8String
          )
        }
      )
    )

  }
}

object JammerCachedOrLoaded extends JammerCachedOrLoaded
