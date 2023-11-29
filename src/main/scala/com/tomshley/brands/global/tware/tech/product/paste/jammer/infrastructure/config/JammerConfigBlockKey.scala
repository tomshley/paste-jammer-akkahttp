package com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config

import com.tomshley.brands.global.tech.tware.products.hexagonal.lib.config.ConfigEnvOrFile

protected[jammer] class JammerConfigBlockKey(parentBlock: JammerConfigBlocks, keyName: String, defaultValueOption: Option[Any] = None) {
  override def toString: String = {
    Option(ConfigEnvOrFile.config.envOrElseConfig(Seq(parentBlock.toBlockName, keyName).mkString("."))).filter(_.nonEmpty).getOrElse(defaultValueOption.getOrElse(None)).toString
  }
}
