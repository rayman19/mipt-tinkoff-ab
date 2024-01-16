package app

import app.models.Session
import app.operations.{AccountService, Authorization}

object App {
  def main(args: Array[String]): Unit = {
    println("Авторизация")
    val session: Option[Session] = Authorization.authorizeUser
    session match {
      case Some(session) => AccountService.view()
      case _             => ()
    }
  }
}