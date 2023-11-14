package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.util.FilesUtil
import com.tomshley.brands.global.tware.tech.product.paste.common.models.SupportedPasteAssetTypes
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{FileGather, Request, RequestMatch, ResourceFileDirectories}

import java.io.File

sealed trait GatherResourceFiles extends IncomingPort[ResourceFileDirectories, FileGather] with FilesUtil {
  override def execute(inboundModel: ResourceFileDirectories): FileGather = {
    lazy val fallbackDirName = inboundModel.projectResourcesDirNameFallbackOption.fold(
      ifEmpty = ""
    )(dirName => dirName)

    lazy val regex = s".+\\.(${SupportedPasteAssetTypes.values.map(t => t.toFileExtension).distinct.mkString("|")})$$".r

    FileGather(
      (inboundModel.projectResourcesDirNames ++ Seq(inboundModel.pasteDirName, fallbackDirName)).distinct.map { resourceName =>
        getClass.getClassLoader.getResource(resourceName)
      }.map { resourceURL =>
        new File(resourceURL.toURI)
      }.map(
        _.getAbsoluteFile
      ).flatMap { resourceFile =>
        recursiveFileList(_.getName matches regex.regex)(resourceFile)
      }.map(_.getAbsolutePath)*
    )
  }
}

object GatherResourceFiles extends GatherResourceFiles
