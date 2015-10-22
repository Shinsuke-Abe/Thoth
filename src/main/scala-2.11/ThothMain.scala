import java.io.File

import ammonite.ops._
import dirs._
import graphviz.GraphvizImageGenerator
import markdown.{ReadmeGenerator, ThothCustomMarkdownParser}
import pandoc.PandocExecutor
import plantuml.PlantUmlImageGenerator
import common.Constants._

/**
 * @author mao.instantlife at gmail.com
 */
object ThothMain extends App {
  import ThothCommandLineArgs._, common.PathUtils._

  parser.parse(args, CommandLineArgs()) match {
    case Some(commandLineArgs) => {
      implicit val pathset = IOPathSet(commandLineArgs.inputBase, commandLineArgs.outputBase)

      val paths = DocDirectoryParser(commandLineArgs.inputBase)

      // Markdown出力
      paths.foreach { path =>
        // Thothカスタムタグのリプレース
        path.markdowns.foreach { file =>
          write(file toOutputFor Markdown(), ThothCustomMarkdownParser(file))
        }

        // Graphviz画像出力
        path.dots.foreach { file =>
          GraphvizImageGenerator(file, path.base / dots toOutputFor Markdown())
        }

        // PlantUML画像出力
        path.umls.foreach { file =>
          PlantUmlImageGenerator(file, path.base / umls toOutputFor Markdown()) match {
            case Left(e) => throw e
          }
        }

        // そのままコピーするもの
        path.resources.foreach(file => cp(file, file toOutputFor Markdown()))

        // README.mdの出力
        write(
          path.base / README toOutputFor Markdown(),
          ReadmeGenerator(path, paths.filter(_.base / up == path.base)))
      }

      // Officeドキュメント出力
      if (commandLineArgs.isOfficeOutput) {
        paths.flatten(_.markdowns).foreach(file =>
          PandocExecutor(file toOutputFor Markdown(), file / up toOutputFor OfficeDoc()))
      }

      System.exit(0)
    }
    case None => System.exit(99)
  }
}

object ThothCommandLineArgs {
  // コマンドライン引数
  case class CommandLineArgs(inputBase: Path = null, outputBase: Path = null, isOfficeOutput: Boolean = false)

  // コマンドラインパーサ
  val parser = new scopt.OptionParser[CommandLineArgs]("Thoth") {
    head("thoth", "1.0")
    // 入力パス
    opt[File]('i', "inputBase") required() valueName("<directory>") action { (path,c) =>
      c.copy(inputBase = Path(path))
    } validate { path =>
      if(exists! Path(path)) success else failure("Option --inputBase must be exists directory")
    } text("inputBase is a required input directory property")
    // 出力パス
    opt[File]('o', "outputBase") required() valueName("<dirctory>") action { (path,c) =>
      c.copy(outputBase = Path(path))
    } validate { path =>
      if(exists! Path(path)) success else failure("Option --outputBase must be exists directory")
    } text("outputBase is a required output directory property")
    // オフィスファイル出力フラグ
    opt[Unit]("officeOutput") action { (_, c) =>
      c.copy(isOfficeOutput = true)
    } text("officeOutput is convert markdown to docx file flag")
  }
}

