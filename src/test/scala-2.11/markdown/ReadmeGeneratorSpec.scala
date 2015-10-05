package markdown

import java.io.IOException
import java.nio.file.NoSuchFileException

import dirs.DocDirectory
import org.specs2.mutable._
import ammonite.ops._

/**
 * Created by shinsuke-abe on 2015/10/02.
 */
class ReadmeGeneratorSpec extends Specification {

  val targetBaseDir = cwd/'target/"scala-2.11"/"test-classes"/'ReadmeGeneratorSpec

  "createDocumentLink" >> {
    def testSequence(targetFile: Path, expected: String) = {
      ReadmeGenerator.createDocumentLink(targetFile) must equalTo(expected)
    }

    "ファイル内にH1がある時はリンクテキストにする" >> testSequence(
      targetBaseDir/"hastitleheader.md",
      "[テストタイトル](hastitleheader.md)")

    "ファイル名にH1がない時はファイル名のみとする" >> testSequence(
      targetBaseDir/"withouttitleheader.md",
      "[withouttitleheader.md](withouttitleheader.md)")

    "ファイル内にH1が二つ以上ある場合は最初のH1を採用する" >> testSequence(
      targetBaseDir/"hasmultipletitleheader.md",
      "[第１章](hasmultipletitleheader.md)")

    "ファイル内のH1が最初にない場合でも取得することができる" >> testSequence(
      targetBaseDir/"hastitleheaderonsecond.md",
      "[ドキュメントタイトル](hastitleheaderonsecond.md)")

    "指定されたファイルが存在しない場合は例外をスローする" >> {
      ReadmeGenerator.createDocumentLink(targetBaseDir/"notfound.md") must
        throwA[NoSuchFileException]
    }

    "指定されたパスがディレクトリの場合は例外をスローする" >> {
      ReadmeGenerator.createDocumentLink(targetBaseDir) must
        throwA[IOException]
    }
  }

  "createSubDocumentLink" >> {
    "指定されたパスがディレクトリの場合はリンクを作成する" >> {
      ReadmeGenerator.createSubDocumentLink(targetBaseDir/'SubDocument1) must
        equalTo("[SubDocument1](SubDocument1/README.md)")
    }

    "指定されたパスが存在しない場合は例外をスローする" >> {
      ReadmeGenerator.createSubDocumentLink(cwd/'notfound) must
        throwA[IllegalArgumentException]
    }

    "指定されたパスがファイルの場合は例外をスローする" >> {
      ReadmeGenerator.createSubDocumentLink(targetBaseDir/"hastitleheader.md") must
        throwA[IllegalArgumentException]
    }
  }

  "apply" >> {
    "README.mdを生成する" >> {
      val expected =
        """# ReadmeGeneratorSpec
          |
          |## ドキュメント一覧
          |
          |* [第１章](hasmultipletitleheader.md)
          |* [テストタイトル](hastitleheader.md)
          |* [ドキュメントタイトル](hastitleheaderonsecond.md)
          |* [withouttitleheader.md](withouttitleheader.md)
          |
          |## サブドキュメント一覧
          |
          |* [SubDocument1](SubDocument1/README.md)
          |* [SubDocument2](SubDocument2/README.md)""".stripMargin

      val target = DocDirectory(
        targetBaseDir,
        Seq(
          targetBaseDir/"hasmultipletitleheader.md",
          targetBaseDir/"hastitleheader.md",
          targetBaseDir/"hastitleheaderonsecond.md",
          targetBaseDir/"withouttitleheader.md"),
        Seq(),
        Seq())

      val subTargetList =
        List(DocDirectory(targetBaseDir/'SubDocument1, Seq(), Seq(), Seq()),
             DocDirectory(targetBaseDir/'SubDocument2, Seq(), Seq(), Seq()))

      ReadmeGenerator(target, subTargetList) must equalTo(expected)
    }
  }

}
