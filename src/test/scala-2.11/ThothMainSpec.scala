import org.specs2.mutable._
import ammonite.ops._

/**
 * Created by shinsuke-abe on 2015/10/21.
 */
class ThothMainSpec extends Specification {
  "実行時引数" >> {
    import ThothCommandLineArgs._

    val input = cwd/'target/"scala-2.11"/"test-classes"/'ThothMainSpec/'inputBase
    val output = cwd/'target/"scala-2.11"/"test-classes"/'ThothMainSpec/'outputBase

    "-oが省略されたらパース結果がNoneになる" >> {
      val args = Array("-i", input.toString())

      parser.parse(args, CommandLineArgs()) must beNone
    }

    "-iが省略されたらパース結果がNoneになる" >> {
      val args = Array("-o", output.toString())

      parser.parse(args, CommandLineArgs()) must beNone
    }

    "-iが存在しないパスの場合はパース結果がNoneになる" >> {
      val args = Array("-i", "hoge", "-o", output.toString())

      parser.parse(args, CommandLineArgs()) must beNone
    }

    "-oが存在しないパスの場合はパース結果がNoneになる" >> {
      val args = Array("-i", input.toString(), "-o", "fuga")

      parser.parse(args, CommandLineArgs()) must beNone
    }

    "--officeOutputが未指定の場合はfalse" >> {
      val args = Array("-i", input.toString(), "-o", output.toString())

      parser.parse(args, CommandLineArgs()) must beSome(CommandLineArgs(input, output, false))
    }

    "--officeOutputが指定されている場合はtrue" >> {
      val args = Array("-i", input.toString(), "-o", output.toString(), "--officeOutput")

      parser.parse(args, CommandLineArgs()) must beSome(CommandLineArgs(input, output, true))
    }

    "オプションの順序を変更しても正しくパースされる" >> {
      val args = Array("-o", output.toString(), "--officeOutput", "-i", input.toString())

      parser.parse(args, CommandLineArgs()) must beSome(CommandLineArgs(input, output, true))
    }
  }

  // TODO パスの暗黙変換のテスト
}
