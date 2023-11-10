package com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config

protected[jammer] enum JammerConfigBlocks(blockName: String) {
  case JAMMER extends JammerConfigBlocks("paste.jammer")

  def toBlockName: String = blockName
}
