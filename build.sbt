name := "Thoth"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases"

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  "net.sourceforge.plantuml" % "plantuml" % "8031",
  "com.lihaoyi" %% "ammonite-ops" % "0.4.8",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  "com.github.scopt" %% "scopt" % "3.3.0",
  "org.specs2" %% "specs2-core" % "3.6.4" % "test"
)

enablePlugins(DockerPlugin)

docker <<= docker.dependsOn(sbt.Keys.`package`.in(Compile, packageBin))

dockerfile in docker := {
  // 各種設定値生成
  val jarFile = artifactPath.in(Compile, packageBin).value
  val classpath = (managedClasspath in Compile).value
  val mainclass = mainClass.in(Compile, packageBin).value.getOrElse(sys.error("Expected exactly one main class"))
  val jarTarget = s"/app/${jarFile.getName}"
  val classpathString = classpath.files.map("/app/" + _.getName).mkString(":") + ":" + jarTarget

  // Dockerファイル生成
  new Dockerfile {
    from("java")
    add(classpath.files, "/app/")
    add(jarFile, jarTarget)
    run("apt-get", "update")
    run("apt-get", "-y", "install", "graphviz")
    run("apt-get", "-y", "install", "pandoc")
    entryPoint("java", "-cp", classpathString, mainclass, "-i", "/in", "-o", "/out")
  }
}

imageNames in docker := Seq(
  ImageName(s"shinsukeabe/thoth:latest"),
  ImageName(namespace = Some("shinsukeabe"),
    repository = "thoth",
    tag = Some(version.value))
)