package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.Model
import com.tomshley.brands.global.tware.tech.product.paste.common.models.PasteModule

import scala.concurrent.Future

case class JamManifest(
                           pasteModules:Future[Seq[PasteModule]]
                           ) extends Model{

}
