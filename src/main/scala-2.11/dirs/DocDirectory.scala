package dirs

import ammonite.ops.Path

/**
 * Created by shinsuke-abe on 2015/10/01.
 */
case class DocDirectory(base: Path, markdowns: Seq[Path], umls: Seq[Path], resources: Seq[Path], children: Seq[DocDirectory])
