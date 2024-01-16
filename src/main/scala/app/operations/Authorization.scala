package app.operations

import app.config.JsonPaths.usersPath
import app.models.{Session, User, UserJsonUtil}

import scala.annotation.tailrec
import scala.io.StdIn

object Authorization {
  val users: Seq[User] = UserJsonUtil.loadUsersFromJsonFile(usersPath)

  def checkValidFromJson(username: String, password: String): String = {
    users.find(_.username == username) match {
      case Some(user) if user.password == password => "valid"
      case Some(_) => "invalid"
      case None => "new"
    }
  }

  @tailrec
  def authorizeUser: Session = {
    val username = getUsername
    val password = getPassword
    checkValidFromJson(username, password) match {
      case "new" => {
        val newUser = User(username, password)
        val updatedUsers = users :+ newUser
        UserJsonUtil.saveUsersToJsonFile(updatedUsers, usersPath)

        println(s"Вы новый пользователь! Добро пожаловать $username!")
        println()
        Session(username)
      }
      case "valid" => {
        println(s"Добро пожаловать $username!")
        println()
        Session(username)
      }
      case "invalid" => {
        println("Ошибка авторизации. Неправильный пароль к логину")
        println("Повторите авторизацию!")
        println()
        authorizeUser
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