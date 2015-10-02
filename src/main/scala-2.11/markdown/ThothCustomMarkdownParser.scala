package markdown

import ammonite.ops._

/**
 * Created by shinsuke-abe on 2015/10/02.
 */
object ThothCustomMarkdownParser {
  def apply(file: Path) = {
    read! file
  }
}
