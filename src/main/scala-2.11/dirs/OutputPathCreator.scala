package dirs

import ammonite.ops._

/**
 * Created by shinsuke-abe on 2015/10/02.
 */
object OutputPathCreator {
  def apply(target: Path, inputBase: Path, outputBase: Path, outputType: OutputType = Markdown()) =
    outputBase/outputType.dirName/(target relativeTo inputBase)
}

sealed trait OutputType {
  def dirName: String
}

case class Markdown() extends OutputType {
  override def dirName = "markdown"
}

case class OfficeDoc() extends OutputType {
  override def dirName = "office"
}