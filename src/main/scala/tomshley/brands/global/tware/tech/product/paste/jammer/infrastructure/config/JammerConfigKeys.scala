package tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config

protected[jammer] enum JammerConfigKeys(configBlockKey: JammerConfigBlockKey) {
  case JAMMER_CONFIG_EXAMPLE extends JammerConfigKeys(JammerConfigBlockKey(JammerConfigBlocks.JAMMER, "jammerConfigExample", Some(Seq())))
  def toValue: String = configBlockKey.toString
}
