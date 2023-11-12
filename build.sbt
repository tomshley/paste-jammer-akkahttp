lazy val pasteGroupName = "paste"
lazy val commonProjectName = Seq(pasteGroupName, "common").mkString("-")
lazy val jammerProjectName = Seq(pasteGroupName, "jammer").mkString("-")

lazy val pasteLibOrgName = "com.tomshley.brands.global.tware.tech.product.paste"
lazy val commonProjectRef = ProjectRef(file(Seq("..", commonProjectName).mkString("/")), commonProjectName)

lazy val jammerProject = publishableProject(jammerProjectName, Some(file(".")))
  .enablePlugins(
    ProjectTemplatePlugin,
    ProjectsHelperPlugin,
    ProjectStructurePlugin,
    EdgeProjectPlugin
  )
  .settings(
    name := jammerProjectName,
    organization := pasteLibOrgName,
    libraryDependencies ++= Seq(
      "com.yahoo.platform.yui" % "yuicompressor" % "2.4.8",
      "com.google.javascript" % "closure-compiler" % "v20230802",
      "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2",
      "com.typesafe.akka" %% "akka-http-caching" % "10.6.0-M1"
    )
  )
  .dependsOn(
    commonProjectRef
  )
