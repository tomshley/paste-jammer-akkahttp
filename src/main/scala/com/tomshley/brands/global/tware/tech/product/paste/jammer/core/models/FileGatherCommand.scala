package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models


import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingModel, Model}
import com.tomshley.brands.global.tware.tech.product.paste.common.models.SupportedPasteAssetType

case class FileGatherCommand(
                              absPaths: Seq[String], supportedPasteAssetTypesOption: Option[Seq[SupportedPasteAssetType]], buildDirNameOption: Option[String] = None
                     ) extends IncomingModel {

  lazy val supportedPasteAssetTypes: Seq[SupportedPasteAssetType] = {
    supportedPasteAssetTypesOption.fold(
      ifEmpty = SupportedPasteAssetType.values.filter(_ == SupportedPasteAssetType.JS).toSeq
    )(
      supportedTypes => supportedTypes
    )
  }
}
object FileGatherCommand:
  def apply(absPaths:String*) = new FileGatherCommand(
    absPaths, None, None
  )
