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

  def getInputSelector: Int = {
    print("Введите номер: ")
    StdIn.readInt()
  }

  def getAnyButton: String = {
    print("Введите любую строку для выхода из раздела")
    StdIn.readLine()
  }

  def exit(): Nothing = scala.sys.exit()
}