package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import akka.Done
import akka.actor.ActorSystem
import akka.stream.scaladsl.Source
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{Port, PortAsyncExecution}
import com.tomshley.brands.global.tware.tech.product.paste.common.config.PasteCommonConfigKeys
import com.tomshley.brands.global.tware.tech.product.paste.common.models.{PasteModule, PastePart}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.*
import com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config.JammerConfigKeys

import java.nio.file.{Files, Paths}
import scala.concurrent.{ExecutionContext, Future}

sealed trait EnsureBuildDirectories extends Port[ResourceFileDirectoriesCommand, Future[Done]] with PortAsyncExecution[ResourceFileDirectoriesCommand, Future[Done]] {
  given system: ActorSystem = ActorSystem(JammerConfigKeys.JAMMER_ACTOR_SYSTEM_NAME.toValue)

  override def executeAsync(inboundModel: ResourceFileDirectoriesCommand)(implicit ec: ExecutionContext): Future[Done] = {
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

  private def buildDirectoryGathering(fileGatheringModel: FileGatherCommand): Iterator[BuildableFilePath] = {
    fileGatheringModel.absPaths
      .map(
        Paths.get(_)
      )
      .map { sourcePath =>
        val pasteModule = PasteModule(
          part = PastePart.apply(
            sourcePath
          ),
          sourceFile = sourcePath.toFile
        )
        BuildableFilePath(
          pasteModule.sourceFile.toPath,
          pasteModule.expectedOptimizedFile.toPath.getParent
        )
      }.iterator
  }

}

object EnsureBuildDirectories extends EnsureBuildDirectories
