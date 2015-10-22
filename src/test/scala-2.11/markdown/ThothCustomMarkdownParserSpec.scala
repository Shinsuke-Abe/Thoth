package markdown

/**
 * @author mao.instantlife at gmail.com
 */

import ammonite.ops._
import org.specs2.mutable._

class ThothCustomMarkdownParserSpec extends Specification {
  val targetBaseDir = cwd/'target/"scala-2.11"/"test-classes"/'ThothCustomMarkdownParserSpec

  "apply" >> {
    "入力ファイルが存在しない場合は例外をスロー" >> {
      val targetFile = targetBaseDir/"notfound.md"

      ThothCustomMarkdownParser(targetFile) must throwA[Exception]
    }

    "タイトルと代替テキストを省略したpumlマークダウンをパースする" >> {
      val targetFile = targetBaseDir/"withsimplestpumlmarkdown.md"
      val expected = """# タイトル
                       |
                       |テストマークダウンファイル
                       |
                       |## セクション１
                       |
                       |テスト
                       |
                       |!(puml/sample.png)""".stripMargin

      ThothCustomMarkdownParser(targetFile) must equalTo(expected)
    }

    "代替テキストを省略したpumlマークダウンをパースする" >> {
      val targetFile = targetBaseDir/"withtitlepumlmarkdown.md"
      val expected = """# タイトル
                       |
                       |テストマークダウンファイル
                       |
                       |## セクション１
                       |
                       |テスト
                       |
                       |!(puml/sample.png "タイトル")
                       |
                       |コメント""".stripMargin

      ThothCustomMarkdownParser(targetFile) must equalTo(expected)
    }

    "全ての指定を省略しないpumlマークダウンをパースする" >> {
      val targetFile = targetBaseDir/"pumlmarkdown.md"
      val expected = """# タイトル
                       |
                       |テストマークダウンファイル
                       |
                       |## セクション１
                       |
                       |テスト
                       |
                       |![代替テキスト](puml/sample.png "タイトル")
                       |
                       |コメント
                       |
                       |コメント""".stripMargin

      ThothCustomMarkdownParser(targetFile) must equalTo(expected)
    }

    "タイトルと代替テキストを省略したdotマークダウンをパースする" >> {
      val targetFile = targetBaseDir/"withsimplestdotmarkdown.md"
      val expected = """# タイトル
                       |
                       |テストマークダウンファイル
                       |
                       |## セクション１
                       |
                       |テスト
                       |
                       |!(dot/sample.png)""".stripMargin

      ThothCustomMarkdownParser(targetFile) must equalTo(expected)
    }

    "代替テキストを省略したdotマークダウンをパースする" >> {
      val targetFile = targetBaseDir/"withtitledotmarkdown.md"
      val expected = """# タイトル
                       |
                       |テストマークダウンファイル
                       |
                       |## セクション１
                       |
                       |テスト
                       |
                       |!(dot/sample.png "タイトル")
                       |
                       |コメント""".stripMargin

      ThothCustomMarkdownParser(targetFile) must equalTo(expected)
    }

    "すべての指定を省略しないdotマークダウンをパースする" >> {
      val targetFile = targetBaseDir/"dotmarkdown.md"
      val expected = """# タイトル
                       |
                       |テストマークダウンファイル
                       |
                       |## セクション１
                       |
                       |テスト
                       |
                       |![代替テキスト](dot/sample.png "タイトル")
                       |
                       |コメント
                       |
                       |コメント""".stripMargin

      ThothCustomMarkdownParser(targetFile) must equalTo(expected)
    }

    "カスタムタグがない場合は入力ファイルの内容と同じテキストが返る" >> {
      val targetFile = targetBaseDir/"withoutcustommarkdown.md"
      val expected = read! targetFile

      ThothCustomMarkdownParser(targetFile) must equalTo(expected)
    }
  }
}
