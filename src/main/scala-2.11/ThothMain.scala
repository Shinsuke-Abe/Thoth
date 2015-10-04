import ammonite.ops._
import dirs.{Markdown, OutputPathCreator, DocDirectoryParser, OutputType}
import markdown.{ReadmeGenerator, ThothCustomMarkdownParser}
import plantuml.PlantUmlImageGenerator

/**
 * @author mao.instantlife at gmail.com
 */
object ThothMain extends App {
  val inputDir = args(0)
  implicit val outputDir = Path(args(1))
  val isOfficeOutput = if (args.length > 2) {
    args(3) match {
      case "true" => true
      case _ => false
    }
  } else false

  implicit val inputBaseDir = Path(inputDir)
  val inputDirectories = DocDirectoryParser(inputDir) // TODO 引数の型をPathに変更した方がいい

  inputDirectories.markdowns.foreach{ file =>
    write(
      file toOutputFor Markdown(),
      ThothCustomMarkdownParser(file))
  }

  inputDirectories.umls.foreach{ file =>
    PlantUmlImageGenerator(
      file,
      inputDirectories.base/'umls toOutputFor Markdown()) match {
      case Left(e) => throw e
    }
  }

  inputDirectories.resources.foreach(file => cp(file, file toOutputFor Markdown()))

  write(
    inputBaseDir/"README.md" toOutputFor Markdown(), // TODO 定型的な文字列を固定化する
    ReadmeGenerator(inputDirectories))

  implicit class RichPath(path: Path) {
    def toOutputFor(outputType: OutputType)(implicit inputBase: Path, outputBase: Path) = {
      OutputPathCreator(path, inputBase, outputBase, outputType)
    }
  }
//
//  alt Officeドキュメント生成フラグ=true
//  ThothMain --> PandocCommandGenerator : Pandocコマンド生成
//  PandocCommandGenerator --> ThothMain : Pandocコマンド生成_Response
//
//  ThothMain --> Process: コマンド実行
//  Process --> ThothMain: コマンド実行_Response
//  end
//
//  ThothMain --> ユーザ: 実行_Response
}
