package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.Model

case class FileGather(
                       absPaths: Seq[String], buildDirNameOption: Option[String] = None
                     ) extends Model
