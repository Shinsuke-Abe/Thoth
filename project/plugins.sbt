logLevel := Level.Warn

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.2.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.3")

lazy val root = project.in(file(".")).dependsOn(githubRepo)

lazy val githubRepo = uri("git://github.com/scoverage/sbt-coveralls.git")