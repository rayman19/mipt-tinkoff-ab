package app.ui.console

import sys.process._

object Console {
  def cls(): Unit = {
    val os = sys.props("os.name").toLowerCase
    if (os.contains("win")) {
      "cmd /c cls".!
    } else {
      "clear".!
    }
  }
}