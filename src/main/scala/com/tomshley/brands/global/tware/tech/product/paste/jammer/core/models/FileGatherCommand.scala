package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models


import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{IncomingModel, Model}
import com.tomshley.brands.global.tware.tech.product.paste.common.models.SupportedPasteAssetTypes

case class FileGatherCommand(
                       absPaths: Seq[String], supportedPasteAssetTypesOption: Option[Seq[SupportedPasteAssetTypes]], buildDirNameOption: Option[String] = None
                     ) extends IncomingModel {

  lazy val supportedPasteAssetTypes: Seq[SupportedPasteAssetTypes] = {
    supportedPasteAssetTypesOption.fold(
      ifEmpty = SupportedPasteAssetTypes.values.filter(_ == SupportedPasteAssetTypes.JS).toSeq
    )(
      supportedTypes => supportedTypes
    )
  }
}
object FileGatherCommand:
  def apply(absPaths:String*) = new FileGatherCommand(
    absPaths, None, None
  )
