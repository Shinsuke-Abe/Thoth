package markdown

import ammonite.ops._
import dirs.DocDirectory
import common.Constants._

/**
 * @author mao.instantlife at gmail.com
 */
object ReadmeGenerator {
  def apply(dir: DocDirectory, subDocs: List[DocDirectory] = List()) = {
    val documentLinks = dir.markdowns.map(f => s"* ${createDocumentLink(f)}").mkString("\n")
    val subDocumentLinks = subDocs.map(d => s"* ${createSubDocumentLink(d.base)}").mkString("\n")

    s"""# ${(stat! dir.base).name}
       |
       |## ドキュメント一覧
       |
       |${documentLinks}
       |
       |## サブドキュメント一覧
       |
       |${subDocumentLinks}""".stripMargin
  }

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

    s"[${(stat! dir).name}](${(stat! dir).name}/${README})"
  }
}
