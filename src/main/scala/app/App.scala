package app

import app.config.JsonPaths.{basePath, usernamePath, usersPath}
import app.models.{Session, UserJsonUtil}
import app.models.UserJsonUtil.createEmptyJson
import app.operations.{AccountService, Authorization}

import java.io.File

object App {
  def main(args: Array[String]): Unit = {
    val file = new File(basePath)
    if (!file.exists()) {
      file.mkdir()
    }
    createEmptyJson(usersPath)
    println("Авторизация")
    val session: Option[Session] = Authorization.authorizeUser
    session match {
      case Some(session) => AccountService.view(session)
      case _             => ()
    }
  }
}