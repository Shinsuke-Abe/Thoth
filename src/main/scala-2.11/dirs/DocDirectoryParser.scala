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

    DocDirectory(
      path,
      list.filter(_ hasSpecifiedExtension markdownExt),
      list getSubDirectoriesFiles(dots, Some(dotExt)),
      list getSubDirectoriesFiles(umls, Some(pumlExt)),
      list.getSubDirectoriesFiles(resources).toList ::: list.find(_ hasNotSpecifiedExtension markdownExt).toList)
  }

  implicit class RichLsSeq(pathList: LsSeq) {
    def getSubDirectoriesFiles(dir: Symbol, extFilter: Option[String] = None) = {
      (pathList.find(_ isSpecifiedDirectory dir), extFilter) match {
        case (Some(dir), Some(extFilter)) => ls! dir filter(_ hasSpecifiedExtension extFilter)
        case (Some(dir), None) => ls! dir
        case (None, _) => Seq()
      }
    }
  }
}

