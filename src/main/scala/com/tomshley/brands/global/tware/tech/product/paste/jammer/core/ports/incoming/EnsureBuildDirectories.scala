package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import akka.actor.ActorSystem
import akka.stream.scaladsl.Source
import akka.{Done, NotUsed}
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{Port, PortAsyncExecution}
import com.tomshley.brands.global.tware.tech.product.paste.common.config.PasteCommonConfigKeys
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.*
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerConfigKeys

import java.nio.file.{Files, Paths}
import scala.concurrent.{ExecutionContext, Future}

sealed trait EnsureBuildDirectories extends Port[ResourceFileDirectories, Future[Done]] with PortAsyncExecution[ResourceFileDirectories, Future[Done]] {
  given system: ActorSystem = ActorSystem(JammerConfigKeys.JAMMER_ACTOR_SYSTEM_NAME.toValue)

  override def executeAsync(inboundModel: ResourceFileDirectories)(implicit ec: ExecutionContext): Future[Done] = {
    Source.fromIterator(() =>
      buildDirectoryGathering(
        GatherResourceFiles.execute(inboundModel)
      )
    ).map(p => {
      Files.createDirectories(
        p.buildDirPath
      )
    }).run()
  }

  private def buildDirectoryGathering(fileGatheringModel: FileGather): Iterator[BuildableFilePath] = {
    fileGatheringModel.absPaths
      .map(
        Paths.get(_)
      )
      .map(p =>
        BuildableFilePath(
          p,
          Paths.get(Seq(
            p.getParent,
            fileGatheringModel.buildDirNameOption.fold(
              ifEmpty = PasteCommonConfigKeys.BUILD_DIR_NAME.toValue
            )(name => name)
          ).mkString("/"))
        )
      ).iterator
  }

}

object EnsureBuildDirectories extends EnsureBuildDirectories
