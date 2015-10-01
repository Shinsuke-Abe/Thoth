import org.specs2.mutable._
import ammonite.ops._

/**
 * Created by shinsuke-abe on 2015/10/01.
 */
class DocDirectoryParserSpec extends Specification {
  val targetBaseDir = cwd/'target/"scala-2.11"/"test-classes"/'DocDirectoryParserSpec

  "apply" >> {
    "markdownファイルを取得できる" >> {
      val targetDir = targetBaseDir/'getmarkdowns
      val expected = Seq(targetDir/"bar.md", targetDir/"foo.md")

      DocDirectoryParser(targetDir.toString()).markdowns must beSome(expected)
    }
    // TODO umls配下のpumlファイルを取得できる
    // TODO resources配下のファイルを取得できる
    // TODO ルールに沿わないファイルはresoucesのリストに追加
    // TODO 子ディレクトリもパースできる
  }
}
