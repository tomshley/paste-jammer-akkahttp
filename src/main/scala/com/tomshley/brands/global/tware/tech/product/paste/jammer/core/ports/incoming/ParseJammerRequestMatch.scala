package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.ports.incoming

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.IncomingPort
import com.tomshley.brands.global.tware.tech.product.paste.common.models.{PasteModule, PastePart, SinkDependencyCommand, SupportedPasteAssetType}
import com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models.{CachedOrLoadedEnvelope, HTTPAssetType, ParsedRequestCommand, RequestMatchCommand}

sealed trait ParseJammerRequestMatch extends IncomingPort[RequestMatchCommand, CachedOrLoadedEnvelope] {
  private def matchManifestModuleFromPart(pastePart: PastePart) = {
    PasteModule(
      part = pastePart, sourceResourcePath = "paste/scripts" + pastePart.name + ".js"
    )
  }

  override def execute(inboundModel: RequestMatchCommand): CachedOrLoadedEnvelope = {

    lazy val httpAssetTypeValues = HTTPAssetType.values
    lazy val httpAssetType = httpAssetTypeValues.find(
      _.toExtension == inboundModel.fileExtension
    ).get

    CachedOrLoadedEnvelope(
      inboundModel.jammerRequest,
      httpAssetType,
      SinkDependencyCommand(
        inboundModel.jamPathString.split(",").map(s =>
          matchManifestModuleFromPart(PastePart(
            s,
            httpAssetType.toAssetType
          ))
        ).toSeq,
        httpAssetTypeValues.map(_.toAssetType)
      )
    )
  }
}

object ParseJammerRequestMatch extends ParseJammerRequestMatch
