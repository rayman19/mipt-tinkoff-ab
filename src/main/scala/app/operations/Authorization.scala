package app.operations

import app.config.JsonPaths.usersPath
import app.errors.ErrorMessages.{errorMessageAuthInvalidUsernameAndPass, errorMessageAuthInvalidPass}
import app.models.{Session, User, UserJsonUtil}
import app.ui.Console.getInputSelector

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
  def authorizeUser: Option[Session] = {
    val username = getUsername
    val password = getPassword
    checkValidFromJson(username, password) match {
      case "new" => {
        errorMessageAuthInvalidUsernameAndPass()
        getInputSelector match {
          case "1" => {
            val newUser = User(username, password)
            val updatedUsers = users :+ newUser
            UserJsonUtil.saveUsersToJsonFile(updatedUsers, usersPath)
            println(s"Вы новый пользователь! Добро пожаловать $username!")
            println()
            Some(Session(username))
          }
          case "2" => authorizeUser
          case _   => None
        }
      }
      case "valid" => {
        println(s"Добро пожаловать $username!")
        println()
        Some(Session(username))
      }
      case "invalid" => {
        errorMessageAuthInvalidPass()
        getInputSelector match {
          case "0" => None
          case _   => authorizeUser
        }
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