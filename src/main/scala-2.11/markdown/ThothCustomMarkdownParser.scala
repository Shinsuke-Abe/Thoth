package markdown

import ammonite.ops._

import scala.util.parsing.combinator.RegexParsers

/**
 * Created by shinsuke-abe on 2015/10/02.
 */
object ThothCustomMarkdownParser extends RegexParsers {
  lazy val pumlMarkdown = mark ~> opt(alterText) ~ pumlFile ^^ {
    case Some(altertext) ~ pumlFile => "!" + altertext.mkString("[", "", "]") + pumlFile
    case None ~ pumlFile => "!" + pumlFile}

  lazy val pumlFile = "(" ~> """.+\.puml""".r ~ opt(title) <~ ")" ^^ {
    case file ~ Some(title) => "(" + file.replace(".puml", ".png") + " " + title.mkString("\"", "", "\"") + ")"
    case file ~ None => "(" + file.replace(".puml", ".png") + ")"
  }

  lazy val alterText = "[" ~> "[^\\[\\]]*".r <~ "]"
  lazy val title = "\"" ~> "[^\"]*".r <~ "\""
  lazy val mark = "$"

  def apply(file: Path) = (read! file).split("\n").toList.map(line =>
      parseAll(pumlMarkdown, line) match {
        case Success(result, _) => result
        case NoSuccess(msg, text) => line
      }
    ).mkString("\n")
}
