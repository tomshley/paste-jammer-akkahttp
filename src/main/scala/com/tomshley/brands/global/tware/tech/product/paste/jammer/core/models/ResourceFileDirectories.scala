package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.{Model, IncomingModel}
import com.tomshley.brands.global.tware.tech.product.paste.common.config.PasteCommonConfigKeys

case class ResourceFileDirectories(
                                    pasteDirName: String = PasteCommonConfigKeys.PASTE_ROOT.toValue,
                                    projectResourcesDirNames: Seq[String] = Seq(),
                                    projectResourcesDirNameFallbackOption: Option[String] = None

                                  ) extends Model with IncomingModel
