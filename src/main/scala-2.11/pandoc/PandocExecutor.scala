package pandoc

import ammonite.ops._

/**
 * Created by shinsuke-abe on 2015/10/02.
 */
object PandocExecutor {

  def generateArgs(inputDir: Path, outputDir: Path) = {
    require(exists! inputDir && (stat! inputDir).isFile)

    val outputFileName = (stat! inputDir).name.replaceAll(".md", ".docx")
    List("-o", (outputDir/outputFileName).toString(), inputDir.toString)
  }
}
