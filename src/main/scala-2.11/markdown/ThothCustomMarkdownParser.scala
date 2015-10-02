package markdown

import ammonite.ops._

import scala.util.parsing.combinator.RegexParsers

/**
 * Created by shinsuke-abe on 2015/10/02.
 */
object ThothCustomMarkdownParser extends RegexParsers {
  lazy val pumlMarkdown = mark ~> pumlFile ^^ { "!" + _ }
  lazy val pumlFile = "(" ~> """.+\.puml""".r <~ ")" ^^ { "(" + _.replace(".puml", ".png") + ")" }
  lazy val mark = "$"

  def apply(file: Path) = (read! file).split("\n").toList.map(line =>
      parseAll(pumlMarkdown, line) match {
        case Success(result, _) => result
        case NoSuccess(msg, text) => line
      }
    ).mkString("\n")
}
