package graphviz

import ammonite.ops._
import common.CommandExecutor
import common.Constants._

/**
 * Created by shinsuke-abe on 2015/10/22.
 */
object GraphvizImageGenerator extends CommandExecutor {
  override def generateArgs(inputDir: Path, outputDir: Path) =
    List("-Tpng", inputDir.toString, "-o", (outputDir/outputFileName(inputDir)).toString)

  override def executeCommand(args: List[String])(implicit implicitWd: Path) = %%dot(args)

  override def inputExt = dotExt

  override def outputExt = pngExt
}
