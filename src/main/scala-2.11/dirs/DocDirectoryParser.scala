package dirs

import ammonite.ops._
/**
 * Created by shinsuke-abe on 2015/10/01.
 */
object DocDirectoryParser {
  def apply(path: String): DocDirectory = {
    val list = ls! Path(path)

    val markdowns = list.filter(_ hasSpecifiedExtension ".md")

    val umls = list.find(_ isSpecifiedDirectory "umls") match {
      case Some(umls) => ls! umls filter(_ hasSpecifiedExtension ".puml")
      case None => Seq()
    }

    val resources = list.find(_ isSpecifiedDirectory "resources") match {
      case Some(resources) => ls! resources
      case None => Seq()
    }

    DocDirectory(markdowns, umls, resources)
  }

  implicit class RichPath(path: Path) {
    def hasSpecifiedExtension(extension: String) = (stat! path).isFile && (stat! path).name.endsWith(extension)

    def isSpecifiedDirectory(name: String) = (stat! path).isDir && (stat! path).name == name
  }
}

