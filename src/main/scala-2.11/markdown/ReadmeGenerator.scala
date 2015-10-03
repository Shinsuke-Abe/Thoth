package markdown

import ammonite.ops._

/**
 * @author mao.instantlife at gmail.com
 */
object ReadmeGenerator {
  def createDocumentLink(file: Path) = (read! file).split("\n").toList.find(_.startsWith("# ")) match {
    case Some(headerline) => {
      val title = headerline.replaceAll("# ", "")

      s"[$title](${(stat! file).name})"
    }
    case None => {
      val fileName = (stat! file).name

      s"[$fileName]($fileName)"
    }
  }

  def createSubDocumentLink(dir: Path) = {
    require(exists! dir && (stat! dir).isDir)

    s"[${(stat! dir).name}](${(stat! dir).name}/README.md)"
  }
}
