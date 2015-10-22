package markdown

import ammonite.ops._
import common.Constants._

import scala.util.parsing.combinator.RegexParsers

/**
 * Created by shinsuke-abe on 2015/10/02.
 */
object ThothCustomMarkdownParser extends RegexParsers {
  lazy val customMarkdowns = pumlMarkdown | dotMarkdown

  // PlantUMLファイル用のカスタムタグ
  lazy val pumlMarkdown = pumlMark ~> opt(alterText) ~ pumlFile ^^ {
    case altertext ~ pumlFile => makeImageTag(pumlFile, altertext)
  }

  lazy val pumlFile = "(" ~> """.+\.puml""".r ~ opt(title) <~ ")" ^^ {
    case file ~ title => replaceFileExtension(file, title, pumlExt)
  }

  // Graphvizファイル用のカスタムタグ
  lazy val dotMarkdown = dotMark ~> opt(alterText) ~ dotFile ^^ {
    case altertext ~ dotFile => makeImageTag(dotFile, altertext)
  }

  lazy val dotFile = "(" ~> """.+\.dot""".r ~ opt(title) <~ ")" ^^ {
    case file ~ title => replaceFileExtension(file, title, dotExt)
  }

  lazy val alterText = "[" ~> "[^\\[\\]]*".r <~ "]"
  lazy val title = "\"" ~> "[^\"]*".r <~ "\""
  lazy val pumlMark = "$"
  lazy val dotMark = "."

  private def makeImageTag(file: String, alter: Option[String]) = alter match {
    case Some(alter) => "!"+ alter.mkString("[", "", "]") + file
    case None =>  "!" + file
  }

  private def replaceFileExtension(file: String, title: Option[String], targetExt: String) = title match {
    case Some(title) => "(" + file.replace(targetExt, pngExt) + " " + title.mkString("\"", "", "\"") + ")"
    case None => "(" + file.replace(targetExt, pngExt) + ")"
  }

  def apply(file: Path) = (read! file).split("\n").toList.map(line =>
      parseAll(customMarkdowns, line) match {
        case Success(result, _) => result
        case NoSuccess(msg, text) => line
      }
    ).mkString("\n")
}
