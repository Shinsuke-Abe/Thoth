package dirs

import ammonite.ops._
import org.specs2.mutable._

/**
 * Created by shinsuke-abe on 2015/10/01.
 */
class DocDirectoryParserSpec extends Specification {
  val targetBaseDir = cwd/'target/"scala-2.11"/"test-classes"/'DocDirectoryParserSpec

  "apply" >> {
    "存在しないディレクトリを指定した場合は例外となる" >> {
      DocDirectoryParser((targetBaseDir/'notfound)) must throwA[Exception]
    }

    "指定されたディレクトリのパスを取得できる" >> {
      val targetDir = targetBaseDir/'getmarkdowns

      DocDirectoryParser(targetDir).base must equalTo(targetDir)
    }

    "markdownファイルを取得できる" >> {
      val targetDir = targetBaseDir/'getmarkdowns
      val expected = Seq(targetDir/"bar.md", targetDir/"foo.md")

      DocDirectoryParser(targetDir).markdowns must equalTo(expected)
    }

    "umls配下のpumlファイルを取得できる" >> {
      val targetDir = targetBaseDir/'getumls
      val expected = Seq(targetDir/'umls/"sample1.puml", targetDir/'umls/"sample2.puml")

      DocDirectoryParser(targetDir).umls must equalTo(expected)
    }

    "resources配下のファイルを取得できる" >> {
      val targetDir = targetBaseDir/'getresources
      val expected = Seq(targetDir/'resources/"sample1.txt", targetDir/'resources/"sample2.txt")

      DocDirectoryParser(targetDir).resources must equalTo(expected)
    }

    "ルールに沿わないファイルはresoucesのリストに追加" >> {
      val targetDir = targetBaseDir/'getresourcesplus
      val expected = Seq(targetDir/'resources/"sample1.txt", targetDir/'resources/"sample2.txt", targetDir/"sample3.txt")

      DocDirectoryParser(targetDir).resources must equalTo(expected)
    }

    "子ディレクトリもパースできる" >> {
      val targetDir = targetBaseDir/'haschilddir
      val expectedMarkdown = Seq(targetDir/'child/"barchild.md", targetDir/'child/"foochild.md")
      val expectedUmls = Seq(targetDir/'child/'umls/"sample1.puml", targetDir/'child/'umls/"sample2.puml")

      val actual =  DocDirectoryParser(targetDir)

      actual.children must equalTo(Seq(DocDirectory(targetDir/'child, expectedMarkdown, expectedUmls, Seq(), Seq())))
    }
  }
}
