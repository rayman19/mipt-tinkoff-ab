package app

import app.config.JsonPaths.{basePath, usernamePath, usersPath}
import app.models.Session
import app.models.UserJsonUtil.createEmptyJson
import app.operations.{AccountService, Authorization}

object App {
  def main(args: Array[String]): Unit = {
    createEmptyJson(usersPath)
    println("Авторизация")
    val session: Option[Session] = Authorization.authorizeUser
    session match {
      case Some(session) => AccountService.view(session)
      case _             => ()
    }
  }
}