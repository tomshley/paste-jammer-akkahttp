package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.outgoing

import akka.actor.ActorSystem
import akka.http.caching.LfuCache
import akka.http.caching.scaladsl.{Cache, CachingSettings}
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingPort, OutgoingPort, PortAsyncExecution}
import com.tomshley.brands.global.tware.tech.product.paste.common.config.PasteCommonConfigKeys
import com.tomshley.brands.global.tware.tech.product.paste.common.models.SourcedDependenciesCommand
import com.tomshley.brands.global.tware.tech.product.paste.common.ports.incoming.SinkDependency
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{CachedOrLoadedEnvelope, ResponseBodyEvent}

import scala.concurrent.ExecutionContext

sealed trait CachedOrLoaded extends OutgoingPort[CachedOrLoadedEnvelope, ResponseBodyEvent] with PortAsyncExecution[CachedOrLoadedEnvelope, ResponseBodyEvent] {
  private lazy val defaultCachingSettings = CachingSettings(system)
  private lazy val lfuCacheSettings = defaultCachingSettings.lfuCacheSettings
    .withInitialCapacity(PasteCommonConfigKeys.HTTP_LFU_CACHE_INITIAL_SIZE.toValueString.toInt)
    .withMaxCapacity(PasteCommonConfigKeys.HTTP_LFU_CACHE_MAX_CAPACITY.toValueString.toInt)
  private lazy val cachingSettings =
    defaultCachingSettings.withLfuCacheSettings(lfuCacheSettings)
  private lazy val cachePaste: Cache[String, String] = LfuCache(cachingSettings)

  given system: ActorSystem = ActorSystem(PasteCommonConfigKeys.HTTP_LFU_CACHE_NAME.toValue)

  override def executeAsync(inboundModel: CachedOrLoadedEnvelope)(implicit ec: ExecutionContext): ResponseBodyEvent = {

    lazy val sinkDependency = SinkDependency.executeAsync(
      inboundModel.sinkDependencyCommand
    )

    ResponseBodyEvent(
      inboundModel.jammerRequest,
      inboundModel.httpAssetType,
      cachePaste.getOrLoad(
        inboundModel.jammerRequest.jamPathStringWithExtURLPart,
        _ => {
          sinkDependency.byteStringFuture.map(
            _.utf8String
          )
        }
      )
    )

  }
}

object CachedOrLoaded extends CachedOrLoaded
