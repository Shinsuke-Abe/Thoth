import ammonite.ops._
/**
 * Created by shinsuke-abe on 2015/10/01.
 */
object DocDirectoryParser {
  def apply(path: String): DocDirectory = {
    val list = ls! Path(path)

    val markdowns = list.filter(p => (stat! p).isFile && (stat! p).name.endsWith(".md"))

    val umls = list.find(p => (stat! p).isDir && (stat! p).name == "umls") match {
      case Some(umls) => ls! umls filter(p => (stat! p).isFile && (stat! p).name.endsWith(".puml"))
      case None => Seq()
    }

    DocDirectory(markdowns, umls)
  }
}
