package app.operations

import app.ui.console.Console.cls
import scala.annotation.tailrec
import scala.io.StdIn

case class Session(username: String, password: String) {

}

object Authorization {
  @tailrec
  def startAuth(): Session = {
    def checkValidFromDb(username: String, password: String): String = {
      // заглушка
      "invalid"
    }

    val username = getUsername
    val password = getPassword
    checkValidFromDb(username, password) match {
      case "new" => {
        println(s"Вы новый пользователь! Добро пожаловать $username!")
        println()
        Session(username, password)
      }
      case "valid" => {
        println(s"Добро пожаловать $username!")
        println()
        Session(username, password)
      }
      case "invalid" => {
        println("Ошибка авторизации. Неправильный пароль к логину")
        println("Повторите авторизацию!")
        println()
        // он почему-то не работает
        // cls()
        startAuth()
      }
    }
  }

  def getUsername: String = {
    print("Введите логин: ")
    StdIn.readLine()
  }

  def getPassword: String = {
    print("Введите пароль: ")
    StdIn.readLine()
  }
}