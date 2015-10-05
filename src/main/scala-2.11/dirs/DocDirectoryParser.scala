package dirs

import ammonite.ops._
/**
 * Created by shinsuke-abe on 2015/10/01.
 */
object DocDirectoryParser {
  def apply(path: Path): DocDirectory = parseDir(path)

  def parseDir(path: Path): DocDirectory = {
    val list = ls! path

    val markdowns = list.filter(_ hasSpecifiedExtension ".md")

    val umls = list.find(_ isSpecifiedDirectory "umls") match {
      case Some(umls) => ls! umls filter(_ hasSpecifiedExtension ".puml")
      case None => Seq()
    }

    val resources = list.find(_ isSpecifiedDirectory "resources") match {
      case Some(resources) => ls! resources
      case None => Seq()
    }

    val filesOutOfRules = list.find(_ hasNotSpecifiedExtension ".md")

    val children = list.filter(p => (stat! p).isDir && (stat! p).name != "umls" && (stat! p).name != "resources")

    if(children.isEmpty) DocDirectory(path, markdowns, umls, resources.toList ::: filesOutOfRules.toList, Seq())
    else DocDirectory(path, markdowns, umls, resources.toList ::: filesOutOfRules.toList, children.map(p => parseDir(p)))
  }

  implicit class RichPath(path: Path) {
    def hasSpecifiedExtension(extension: String) = (stat! path).isFile && (stat! path).name.endsWith(extension)

    def hasNotSpecifiedExtension(extension: String) = (stat! path).isFile && !(stat! path).name.endsWith(extension)

    def isSpecifiedDirectory(name: String) = (stat! path).isDir && (stat! path).name == name
  }
}

