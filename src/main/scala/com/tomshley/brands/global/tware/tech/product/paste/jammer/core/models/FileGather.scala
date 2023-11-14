package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.Model
import com.tomshley.brands.global.tware.tech.product.paste.common.models.SupportedPasteAssetTypes

case class FileGather(
                       absPaths: Seq[String], supportedPasteAssetTypesOption: Option[Seq[SupportedPasteAssetTypes]], buildDirNameOption: Option[String] = None
                     ) extends Model {

  lazy val supportedPasteAssetTypes: Seq[SupportedPasteAssetTypes] = {
    supportedPasteAssetTypesOption.fold(
      ifEmpty = SupportedPasteAssetTypes.values.filter(_ == SupportedPasteAssetTypes.JS).toSeq
    )(
      supportedTypes => supportedTypes
    )
  }
}
object FileGather:
  def apply(absPaths:String*) = new FileGather(
    absPaths, None, None
  )
