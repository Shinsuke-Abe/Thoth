package dirs

import ammonite.ops._
import org.specs2.mutable._
import common.Constants._

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

      DocDirectoryParser(targetDir).head.base must equalTo(targetDir)
    }

    "markdownファイルを取得できる" >> {
      val targetDir = targetBaseDir/'getmarkdowns
      val expected = Seq(targetDir/"bar.md", targetDir/"foo.md")

      DocDirectoryParser(targetDir).head.markdowns must equalTo(expected)
    }

    "dots配下のdotファイルを取得できる" >> {
      val targetDir = targetBaseDir/'getdots
      val expected = Seq(targetDir/dots/"sample1.dot", targetDir/dots/"sample2.dot")

      DocDirectoryParser(targetDir).head.dots must equalTo(expected)
    }

    "umls配下のpumlファイルを取得できる" >> {
      val targetDir = targetBaseDir/'getumls
      val expected = Seq(targetDir/umls/"sample1.puml", targetDir/umls/"sample2.puml")

      DocDirectoryParser(targetDir).head.umls must equalTo(expected)
    }

    "resources配下のファイルを取得できる" >> {
      val targetDir = targetBaseDir/'getresources
      val expected = Seq(targetDir/resources/"sample1.txt", targetDir/resources/"sample2.txt")

      DocDirectoryParser(targetDir).head.resources must equalTo(expected)
    }

    "ルールに沿わないファイルはresoucesのリストに追加" >> {
      val targetDir = targetBaseDir/'getresourcesplus
      val expected = Seq(targetDir/resources/"sample1.txt", targetDir/resources/"sample2.txt", targetDir/"sample3.txt")

      DocDirectoryParser(targetDir).head.resources must equalTo(expected)
    }

    "子ディレクトリもパースできる" >> {
      val targetDir = targetBaseDir/'haschilddir
      val targetChildDir = targetDir/'child

      val expectedMarkdown = Seq(targetDir/"bar.md", targetDir/"foo.md")
      val expectedChildMarkdown = Seq(targetChildDir/"barchild.md", targetChildDir/"foochild.md")
      val expectedChildUml = Seq(targetChildDir/umls/"sample1.puml", targetChildDir/umls/"sample2.puml")

      DocDirectoryParser(targetDir) must equalTo(
        List(DocDirectory(targetDir, expectedMarkdown, Seq(), Seq(), Seq()),
            DocDirectory(targetChildDir, expectedChildMarkdown, Seq(), expectedChildUml, Seq())))
    }
  }
}
