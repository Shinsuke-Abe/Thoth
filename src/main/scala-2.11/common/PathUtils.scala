package common

import java.io.File

import ammonite.ops.{stat, Path}
import common.Constants._
import dirs.{OutputPathCreator, OutputType}

/**
 * Created by shinsuke-abe on 2015/10/21.
 */
object PathUtils {
  case class IOPathSet(inputBase: Path, outputBase: Path)

  implicit class RichPath(path: Path) {
    def toOutputFor(outputType: OutputType)(implicit pathset: IOPathSet) = {
      OutputPathCreator(path, pathset.inputBase, pathset.outputBase, outputType)
    }

    def toFile = new File(path.toString())

    def hasSpecifiedExtension(extension: String) = (stat! path).isFile && (stat! path).name.endsWith(extension)

    def hasNotSpecifiedExtension(extension: String) = (stat! path).isFile && !(stat! path).name.endsWith(extension)

    def isSpecifiedDirectory(name: Symbol) = (stat! path).isDir && Symbol((stat! path).name) == name

    def isSubDirectory = (stat! path).isDir && !excludeSubDirectories.exists(_ == Symbol((stat! path).name))
  }
}
