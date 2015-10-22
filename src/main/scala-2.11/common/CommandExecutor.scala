package common

import ammonite.ops._

/**
 * Created by shinsuke-abe on 2015/10/22.
 */
trait CommandExecutor {
  def apply(inputDir: Path, outputDir: Path) = {
    require(exists! inputDir && (stat! inputDir).isFile)

    implicit val wd: Path = inputDir/up

    if(exists! outputDir == false) mkdir! outputDir

    executeCommand(generateArgs(inputDir, outputDir))
  }

  def outputFileName(inputDir: Path) = (stat! inputDir).name.replaceAll(inputExt, outputExt)

  def inputExt: String

  def outputExt: String

  def executeCommand(args: List[String])(implicit implicitWd: Path): CommandResult

  def generateArgs(inputDir: Path, outputDir: Path): List[String]
}
