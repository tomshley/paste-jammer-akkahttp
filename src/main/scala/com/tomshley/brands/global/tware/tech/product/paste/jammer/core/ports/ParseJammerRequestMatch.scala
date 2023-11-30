package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import com.tomshley.brands.global.tware.tech.product.paste.common.models.{PasteAssetType, PasteModule, PastePart, SinkDependencyCommand}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{CachedOrLoadedEnvelope, HTTPAssetType, ParsedRequestCommand, RequestMatchCommand}

sealed trait ParseJammerRequestMatch extends IncomingPort[RequestMatchCommand, CachedOrLoadedEnvelope] {
  override def execute(inboundModel: RequestMatchCommand): CachedOrLoadedEnvelope = {

    lazy val httpAssetTypeValues = HTTPAssetType.values
    lazy val httpAssetType = httpAssetTypeValues.find(
      _.toExtension == inboundModel.fileExtension
    ).get

    CachedOrLoadedEnvelope(
      inboundModel.jammerRequest,
      httpAssetType,
      SinkDependencyCommand(
        inboundModel.pasteManifest,
        httpAssetTypeValues.map(_.toAssetType)
      )
    )
  }

  private def matchManifestModuleFromPart(pastePart: PastePart) = {
    PasteModule(
      part = pastePart, sourceResourcePath = "paste/scripts" + pastePart.name + ".js"
    )
  }
}

object ParseJammerRequestMatch extends ParseJammerRequestMatch
