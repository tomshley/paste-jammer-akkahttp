lazy val pasteGroupName = "paste"
lazy val commonProjectName = Seq(pasteGroupName, "common").mkString("-")
lazy val jammerProjectName = Seq(pasteGroupName, "jammer").mkString("-")

lazy val pasteLibOrgName = "tomshley.brands.global.tware.tech.product.paste"
lazy val commonProjectRef = ProjectRef(file(Seq("..", commonProjectName).mkString("/")), commonProjectName)

lazy val jammerProject = publishableProject(jammerProjectName, Some(file(".")))
  .enablePlugins(ProjectTemplatePlugin, ProjectsHelperPlugin, ProjectStructurePlugin, LibProjectAkkaHttpPlugin, LibManagedProjectPlugin)
  .settings(
    name := jammerProjectName,
    organization := pasteLibOrgName
  )
  .dependsOn(
    commonProjectRef
  )
