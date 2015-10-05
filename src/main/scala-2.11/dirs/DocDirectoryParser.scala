package dirs

import ammonite.ops._
/**
 * Created by shinsuke-abe on 2015/10/01.
 */
object DocDirectoryParser {
  def apply(path: Path): List[DocDirectory] = mapDirs(List(path))

  def mapDirs(paths: List[Path], docDirs: List[DocDirectory] = List()): List[DocDirectory] = {
    val children = paths.map(path =>
      (ls! path).filter(p => (stat! p).isDir && (stat! p).name != "umls" && (stat! p).name != "resources")).flatten

    val addedDir = docDirs ::: paths.map(parseDir(_))

    if(children.isEmpty) addedDir
    else mapDirs(children, addedDir)
  }

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

    DocDirectory(path, markdowns, umls, resources.toList ::: filesOutOfRules.toList)
  }

  implicit class RichPath(path: Path) {
    def hasSpecifiedExtension(extension: String) = (stat! path).isFile && (stat! path).name.endsWith(extension)

    def hasNotSpecifiedExtension(extension: String) = (stat! path).isFile && !(stat! path).name.endsWith(extension)

    def isSpecifiedDirectory(name: String) = (stat! path).isDir && (stat! path).name == name
  }
}

