package markdown

/**
 * @author mao.instantlife at gmail.com
 */

import ammonite.ops._
import org.specs2.mutable._

class ThothCustomMarkdownParserSpec extends Specification {
  "apply" >> {
    // TODO 入力ファイルが存在しない場合は例外をスロー
    // TODO 記法は &[代替テキスト](pumlファイルのパス "タイトル") とする
    // TODO 代替テキストは省略可能
    // TODO タイトルは省略可能
    // TODO pumlがルールに従って存在しない場合は例外をスロー
    // TODO pumlがルール通りに存在する場合はイメージのマークダウンに書き換える
    "カスタムタグがない場合は入力ファイルの内容と同じテキストが返る" >> {
      val targetFile = cwd/'target/"scala-2.11"/"test-classes"/'ThothCustomMarkdownParserSpec/"withoutcustommarkdown.md"
      val expected = read! targetFile

      ThothCustomMarkdownParser(targetFile) must equalTo(expected)
    }
  }
}
