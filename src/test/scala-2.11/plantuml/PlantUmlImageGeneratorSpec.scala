package plantuml

import org.specs2.mutable._
import ammonite.ops._
import common.Constants._

/**
 * Created by shinsuke-abe on 2015/10/02.
 */
class PlantUmlImageGeneratorSpec extends Specification {
  val targetBaseDir = cwd/'target/"scala-2.11"/"test-classes"/'PlantUmlImageGeneratorSpec/'input/'umls

  "apply" >> {
    "入力パスに指定したファイルが存在しない場合は例外をスローする" >> {
      val targetFile = targetBaseDir/"notfound.puml"
      val tmp = Path(Path.makeTmp)

      PlantUmlImageGenerator(targetFile, tmp) must throwA[IllegalArgumentException]
    }

    "指定した入力パスがディレクトリの場合は例外をスローする" >> {
      val targetFile = targetBaseDir
      val tmp = Path(Path.makeTmp)

      PlantUmlImageGenerator(targetFile, tmp) must throwA[IllegalArgumentException]
    }

    "入力パスに指定したファイルがPlantUmlの形式になっていない場合は例外をスローする" >> {
      val targetFile = targetBaseDir/"badsample.puml"
      val tmp = Path(Path.makeTmp)/'output/'markdown/umls

      PlantUmlImageGenerator(targetFile, tmp) must beLeft
    }

    "シンタックスエラーがある入力ファイルを指定した場合は例外をスローする" >> {
      val targetFile = targetBaseDir/"badsyntax.puml"
      val tmp = Path(Path.makeTmp)/'output/'markdown/umls

      PlantUmlImageGenerator(targetFile, tmp) must beLeft
    }

    "シンタックスエラーのない入力ファイルを指定した場合は変換される" >> {
      val targetFile = targetBaseDir/"goodsyntax.puml"
      val tmp = Path(Path.makeTmp)/'output/'markdown/umls

      PlantUmlImageGenerator(targetFile, tmp) must beRight(tmp/"goodsyntax.png")
    }
  }
}
