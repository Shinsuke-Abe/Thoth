package markdown

import java.io.IOException
import java.nio.file.NoSuchFileException

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

  // TODO サブディレクトリがある場合は
  // TODO README.mdを生成する

  // フォーマット
  // # ディレクトリ名目次
  //
  // ## ドキュメント一覧
  //
  // * [H1タイトル](リンク)
  // * ...
  //
  // ## サブドキュメント一覧
  //
  // * [ディレクトリ名](READMEへのリンク)
  // * ...
}
