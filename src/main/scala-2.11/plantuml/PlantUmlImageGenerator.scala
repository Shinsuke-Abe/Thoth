package plantuml

import java.io.File

import ammonite.ops._
import net.sourceforge.plantuml.SourceFileReader

/**
 * Created by shinsuke-abe on 2015/10/02.
 */
object PlantUmlImageGenerator {
  def apply(inputFile: Path, outputDir: Path): Either[Throwable, Path] = {
    require(exists! inputFile && (stat! inputFile).isFile)

    val generatedImages =
      new SourceFileReader(inputFile.toFile, outputDir.toFile).getGeneratedImages

    if (generatedImages.isEmpty) Left(new Exception("生成されたファイルがありません"))
    else {
      val generatedImage = generatedImages.get(0)
      if (generatedImage.lineErrorRaw() > 0) Left(new Exception(generatedImage.getDescription))
      else Right(Path(generatedImage.getPngFile.toString))
    }
  }

  implicit class RichPath(path: Path) {
    def toFile = new File(path.toString())
  }
}
