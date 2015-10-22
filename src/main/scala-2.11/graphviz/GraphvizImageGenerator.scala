package graphviz

import ammonite.ops._
import common.Constants._

/**
 * Created by shinsuke-abe on 2015/10/22.
 */
object GraphvizImageGenerator {
  def apply(inputDir: Path, outputDir: Path) = {
    implicit val wd: Path = inputDir/up

    if(exists! outputDir == false) mkdir! outputDir

    %%dot(generateArgs(inputDir, outputDir))
  }

  def generateArgs(inputDir: Path, outputDir: Path) = {
    require(exists! inputDir && (stat! inputDir).isFile)

    val outputFileName = (stat! inputDir).name.replaceAll(dotExt, pngExt)
    List("-Tpng", inputDir.toString, "-o", (outputDir/outputFileName).toString())
  }

}
