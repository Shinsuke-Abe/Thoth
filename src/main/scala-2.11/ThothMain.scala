import ammonite.ops._
import dirs._
import markdown.{ReadmeGenerator, ThothCustomMarkdownParser}
import pandoc.PandocExecutor
import plantuml.PlantUmlImageGenerator

/**
 * @author mao.instantlife at gmail.com
 */
object ThothMain extends App {
  val inputBase = Path(args(0))
  val outputBase = Path(args(1))
  val isOfficeOutput = if (args.length > 2) {
    args(3) match {
      case "true" => true
      case _ => false
    }
  } else false

  implicit val pathset = IOPathSet(inputBase, outputBase)

  val paths = DocDirectoryParser(inputBase)
  paths.foreach{path =>
    // Thothカスタムタグのリプレース
    path.markdowns.foreach{ file =>
      write(file toOutputFor Markdown(), ThothCustomMarkdownParser(file)) }

    // PlantUML画像出力
    path.umls.foreach{ file =>
      PlantUmlImageGenerator(file, path.base/'umls toOutputFor Markdown()) match {
        case Left(e) => throw e
      }
    }

    // そのままコピーするもの
    path.resources.foreach(file => cp(file, file toOutputFor Markdown()))

    // README.mdの出力
    write(
      path.base/"README.md" toOutputFor Markdown(), // TODO 定型的な文字列を固定化する
      ReadmeGenerator(path, paths.filter(_.base/up == path.base)))
  }

  case class IOPathSet(inputBase: Path, outputBase: Path)

  implicit class RichPath(path: Path) {
    def toOutputFor(outputType: OutputType)(implicit pathset: IOPathSet) = {
      OutputPathCreator(path, pathset.inputBase, pathset.outputBase, outputType)
    }
  }
//
//  alt Officeドキュメント生成フラグ=true
//  ThothMain --> PandocCommandGenerator : Pandocコマンド生成
//  PandocCommandGenerator --> ThothMain : Pandocコマンド生成_Response
}
