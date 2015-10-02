package pandoc

import ammonite.ops._

/**
 * Created by shinsuke-abe on 2015/10/02.
 */
object PandocExecutor {

  def apply(inputDir: Path, outputDir: Path) = {
    implicit val wd: Path = inputDir/up

    if(exists! outputDir == false) mkdir! outputDir

    %%pandoc(generateArgs(inputDir, outputDir))
  }

  def generateArgs(inputDir: Path, outputDir: Path) = {
    require(exists! inputDir && (stat! inputDir).isFile)

    val outputFileName = (stat! inputDir).name.replaceAll(".md", ".docx")
    List("-o", (outputDir/outputFileName).toString(), inputDir.toString)
  }
}
