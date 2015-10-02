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

    // TODO 記法は &[代替テキスト](pumlファイルのパス "タイトル") とする
    // TODO 代替テキストは省略可能
    // TODO タイトルは省略可能
    // TODO pumlがルールに従って存在しない場合は例外をスロー
    // TODO pumlがルール通りに存在する場合はイメージのマークダウンに書き換える
    "カスタムタグがない場合は入力ファイルの内容と同じテキストが返る" >> {
      val targetFile = targetBaseDir/"withoutcustommarkdown.md"
      val expected = read! targetFile

      ThothCustomMarkdownParser(targetFile) must equalTo(expected)
    }
  }
}
