package dirs

import org.specs2.mutable._
import ammonite.ops._

/**
 * Created by shinsuke-abe on 2015/10/02.
 */
class OutputPathCreatorSpec extends Specification{
  val targetBaseDir = cwd/'OutputPathCreatorSpec

  "create" >> {
    "Markdown用の出力パスを作成する" >> {
      val targetDir = targetBaseDir/'input/'target
      val expected = targetBaseDir/'output/'markdown/'target

      OutputPathCreator(targetDir, targetBaseDir/'input, targetBaseDir/'output, Markdown()) must equalTo(expected)
    }

    "Officeドキュメント用の出力パスを作成する" >> {
      val targetDir = targetBaseDir/'input/'sample
      val expected = targetBaseDir/'output/'office/'sample

      OutputPathCreator(targetDir, targetBaseDir/'input, targetBaseDir/'output, OfficeDoc()) must equalTo(expected)
    }
  }
}
