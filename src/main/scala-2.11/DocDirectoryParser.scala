import ammonite.ops._
/**
 * Created by shinsuke-abe on 2015/10/01.
 */
object DocDirectoryParser {
  def apply(path: String): DocDirectory = {
    val list = ls! Path(path)

    val markdowns = list.filter(p => (stat! p).isFile && (stat! p).name.endsWith(".md"))

    DocDirectory(Some(markdowns))
  }
}
