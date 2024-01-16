package app.ui

import scala.io.StdIn
import scala.sys.process._

object Console {
  def cls(): Unit = {
    val os = sys.props("os.name").toLowerCase
    if (os.contains("win")) {
      "cmd /c cls".!
    } else {
      "clear".!
    }
  }

  def getInputSelector: String = {
    print("Введите номер: ")
    StdIn.readLine()
  }
}