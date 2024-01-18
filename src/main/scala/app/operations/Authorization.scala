package app.operations

import app.config.JsonPaths.{basePath, creditAccountsPath, debitAccountsPath, savingsAccountsPath, usernamePath, usersPath}
import app.errors.ErrorMessages.{errorMessageAuthInvalidPass, errorMessageAuthInvalidUsernameAndPass}
import app.models.{AccountJsonUtil, AuthStatus, Invalid, New, Session, User, UserJsonUtil, Valid}
import app.ui.Console.getInputSelector

import scala.io.StdIn

object Authorization {
  val users: Seq[User] = UserJsonUtil.loadUsersFromJsonFile(usersPath)

  def checkValidFromJson(username: String, password: String): AuthStatus = {
    users.find(_.username == username) match {
      case Some(user) if user.password == password => Valid
      case Some(_)                                 => Invalid
      case None                                    => New
    }
  }

  def authorizeUser: Option[Session] = {
    val username = getUsername
    val password = getPassword

    def WorkWithNewUser = {
      def addUser() = {
        val newUser = User(username, password)
        val updatedUsers = users :+ newUser
        UserJsonUtil.saveUsersToJsonFile(updatedUsers, usersPath)
        UserJsonUtil.mkdirUsername(usernamePath(username))
        AccountJsonUtil.createEmptyJson(debitAccountsPath(username))
        AccountJsonUtil.createEmptyJson(creditAccountsPath(username))
        AccountJsonUtil.createEmptyJson(savingsAccountsPath(username))
        println(s"Вы новый пользователь! Добро пожаловать $username!")
        println()
        Some(Session(username))
      }

      errorMessageAuthInvalidUsernameAndPass()
      getInputSelector match {
        case "1" => addUser()
        case "2" => authorizeUser
        case _   => None
      }
    }

    def WorkWithAuthUser = {
      println(s"Добро пожаловать $username!")
      println()
      Some(Session(username))
    }

    def WorkWithInvalidUser = {
      errorMessageAuthInvalidPass()
      getInputSelector match {
        case "0" => None
        case _   => authorizeUser
      }
    }

    checkValidFromJson(username, password) match {
      case New     => WorkWithNewUser
      case Valid   => WorkWithAuthUser
      case Invalid => WorkWithInvalidUser
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