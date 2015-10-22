package graphviz

import ammonite.ops._
import org.specs2.mutable._

import common.Constants._

/**
 * Created by shinsuke-abe on 2015/10/21.
 */
class GraphvizImageGeneratorSpec extends Specification {
  val targetBaseDir = cwd/'target/"scala-2.11"/"test-classes"/'GraphvizImageGeneratorSpec/'input/'dots

  "apply" >> {
    "入力パスに指定したファイルが存在しない場合は例外をスローする" >> {
      val targetFile = targetBaseDir/"notfound.dot"
      val tmp = Path(Path.makeTmp)

      GraphvizImageGenerator(targetFile, tmp) must throwA[IllegalArgumentException]
    }

    "指定した入力パスがディレクトリの場合は例外をスローする" >> {
      val targetFile = targetBaseDir
      val tmp = Path(Path.makeTmp)

      GraphvizImageGenerator(targetFile, tmp) must throwA[IllegalArgumentException]
    }

    "入力パスに指定したファイルがDot言語の形式になっていない場合は例外をスローする" >> {
      val targetFile = targetBaseDir/"badsample.dot"
      val tmp = Path(Path.makeTmp)/'output/'markdown/dots

      GraphvizImageGenerator(targetFile, tmp) must throwA[RuntimeException]
    }

    "シンタックスエラーがある入力ファイルを指定した場合は例外をスローする" >> {
      val targetFile = targetBaseDir/"badsyntax.dot"
      val tmp = Path(Path.makeTmp)/'output/'markdown/dots

      GraphvizImageGenerator(targetFile, tmp) must throwA[RuntimeException]
    }

    "シンタックスエラーのない入力ファイルを指定した場合は変換される" >> {
      val targetFile = targetBaseDir/"goodsyntax.dot"
      val tmp = Path(Path.makeTmp)/'output/'markdown/dots

      GraphvizImageGenerator(targetFile, tmp)

      (exists! tmp/"goodsyntax.png") must beTrue
    }
  }
}
