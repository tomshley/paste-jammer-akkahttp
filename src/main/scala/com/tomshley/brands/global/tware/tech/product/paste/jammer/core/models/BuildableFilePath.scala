package com.tomshley.brands.global.tware.tech.product.paste.jammer.core.models

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.domain.Model

import java.nio.file.Path

case class BuildableFilePath(
                              path: Path, buildDirPath: Path
) extends Model
