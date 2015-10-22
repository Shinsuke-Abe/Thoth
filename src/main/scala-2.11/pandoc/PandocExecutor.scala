package pandoc

import ammonite.ops._
import common.CommandExecutor
import common.Constants._

/**
 * Created by shinsuke-abe on 2015/10/02.
 */
object PandocExecutor extends CommandExecutor {
  override def generateArgs(inputDir: Path, outputDir: Path) =
    List("-o", (outputDir/outputFileName(inputDir)).toString(), inputDir.toString)

  override def executeCommand(args: List[String])(implicit implicitWd: Path) = %%pandoc(args)

  override def inputExt = markdownExt

  override def outputExt = wordExt
}
