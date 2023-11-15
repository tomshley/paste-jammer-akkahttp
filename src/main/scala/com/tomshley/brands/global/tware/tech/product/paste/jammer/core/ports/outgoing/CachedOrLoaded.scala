package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.outgoing

import akka.actor.ActorSystem
import akka.http.caching.LfuCache
import akka.http.caching.scaladsl.{Cache, CachingSettings}
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingPort, OutgoingPort, PortAsyncExecution}
import com.tomshley.brands.global.tware.tech.product.paste.common.config.PasteCommonConfigKeys
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{ResponseBodyFutureEnvelope, SourcedDependenciesCommand}

import scala.concurrent.ExecutionContext

sealed trait JammerCachedOrLoaded extends OutgoingPort[SourcedDependenciesCommand, ResponseBodyFutureEnvelope] with PortAsyncExecution[SourcedDependenciesCommand, ResponseBodyFutureEnvelope] {
  private lazy val defaultCachingSettings = CachingSettings(system)
  private lazy val lfuCacheSettings = defaultCachingSettings.lfuCacheSettings
    .withInitialCapacity(PasteCommonConfigKeys.HTTP_LFU_CACHE_INITIAL_SIZE.toValue.toInt)
    .withMaxCapacity(PasteCommonConfigKeys.HTTP_LFU_CACHE_MAX_CAPACITY.toValue.toInt)
  private lazy val cachingSettings =
    defaultCachingSettings.withLfuCacheSettings(lfuCacheSettings)
  private lazy val cachePaste: Cache[String, String] = LfuCache(cachingSettings)

  given system: ActorSystem = ActorSystem(PasteCommonConfigKeys.HTTP_LFU_CACHE_NAME.toValue)

  override def executeAsync(inboundModel: SourcedDependenciesCommand)(implicit ec: ExecutionContext): ResponseBodyFutureEnvelope = {
    ResponseBodyFutureEnvelope(
      inboundModel.jammerRequest,
      inboundModel.jammerParsedContentType,
      cachePaste.getOrLoad(
        inboundModel.jammerRequest.jamPathStringWithExtURLPart,
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
