package dirs

import ammonite.ops._
import common.Constants._
/**
 * Created by shinsuke-abe on 2015/10/01.
 */
object DocDirectoryParser {
  import common.PathUtils._

  def apply(path: Path): List[DocDirectory] = mapDirs(List(path))

  def mapDirs(paths: List[Path], docDirs: List[DocDirectory] = List()): List[DocDirectory] = {
    val children = paths.map(path => (ls! path).filter(_ isSubDirectory)).flatten

    val addedDir = docDirs ::: paths.map(parseDir(_))

    if(children.isEmpty) addedDir
    else mapDirs(children, addedDir)
  }

  def parseDir(path: Path): DocDirectory = {
    val list = ls! path

    val markdowns = list.filter(_ hasSpecifiedExtension markdownExt)

    val umlfiles = list.find(_ isSpecifiedDirectory umls) match {
      case Some(umls) => ls! umls filter(_ hasSpecifiedExtension pumlExt)
      case None => Seq()
    }

    val resourcefiles = list.find(_ isSpecifiedDirectory resources) match {
      case Some(resources) => ls! resources
      case None => Seq()
    }

    val filesOutOfRules = list.find(_ hasNotSpecifiedExtension markdownExt)

    DocDirectory(path, markdowns, umlfiles, resourcefiles.toList ::: filesOutOfRules.toList)
  }
}

