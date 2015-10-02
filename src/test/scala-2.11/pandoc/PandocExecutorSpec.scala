package pandoc

import org.specs2.mutable._
import ammonite.ops._

/**
 * Created by shinsuke-abe on 2015/10/02.
 */
class PandocExecutorSpec extends Specification {
  "generateArgs" >> {
    "引数リストを作る" >> {
      val inputTarget = cwd/'target/"scala-2.11"/"test-classes"/'PandocExecutorSpec/"test.md"
      val outputTarget = Path(Path.makeTmp)/'target/'office

      val expected = List("-o", (outputTarget/"test.docx").toString, inputTarget.toString)

      PandocExecutor.generateArgs(inputTarget, outputTarget) must equalTo(expected)
    }

    "入力ファイルが存在しない場合は例外をスローする" >> {
      val inputTarget = cwd/"notfound.md"
      val outputTarget = Path(Path.makeTmp)

      PandocExecutor.generateArgs(inputTarget, outputTarget) must throwA[IllegalArgumentException]
    }
  }
}
