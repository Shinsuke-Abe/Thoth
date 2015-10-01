name := "Thoth"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= Seq(
  "net.sourceforge.plantuml" % "plantuml" % "8031",
  "org.specs2" %% "specs2-core" % "3.6.4" % "test"
)