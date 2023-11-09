package tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config

protected[jammer] enum JammerConfigKeys(configBlockKey: JammerConfigBlockKey) {
  case JAMMER_ACTOR_SYSTEM_NAME extends JammerConfigKeys(JammerConfigBlockKey(JammerConfigBlocks.JAMMER, "actorSystemName", Some("pate-jammer-processing")))
  def toValue: String = configBlockKey.toString
}
