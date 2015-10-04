import ammonite.ops._
import dirs._
import markdown.{ReadmeGenerator, ThothCustomMarkdownParser}
import pandoc.PandocExecutor
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
  generateDocuments(DocDirectoryParser(inputDir)) // TODO 引数の型をPathに変更した方がいい
  
  def generateDocuments(docDirectory: DocDirectory) {
    // Thothカスタムタグのリプレース
    docDirectory.markdowns.foreach{ file =>
      write(file toOutputFor Markdown(), ThothCustomMarkdownParser(file)) }

    // PlantUML画像出力
    docDirectory.umls.foreach{ file =>
      PlantUmlImageGenerator(file, docDirectory.base/'umls toOutputFor Markdown()) match {
        case Left(e) => throw e
      }
    }

    // そのままコピーするもの
    docDirectory.resources.foreach(file => cp(file, file toOutputFor Markdown()))

    // README.mdの出力
    write(
      docDirectory.base/"README.md" toOutputFor Markdown(), // TODO 定型的な文字列を固定化する
      ReadmeGenerator(docDirectory))
    
    docDirectory.children.foreach(generateDocuments(_))
  }

  implicit class RichPath(path: Path) {
    def toOutputFor(outputType: OutputType)(implicit inputBase: Path, outputBase: Path) = {
      OutputPathCreator(path, inputBase, outputBase, outputType)
    }
  }
//
//  alt Officeドキュメント生成フラグ=true
//  ThothMain --> PandocCommandGenerator : Pandocコマンド生成
//  PandocCommandGenerator --> ThothMain : Pandocコマンド生成_Response
}
