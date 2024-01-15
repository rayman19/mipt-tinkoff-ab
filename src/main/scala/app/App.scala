package app

import app.operations.Authorization

object App {
  def main(args: Array[String]): Unit = {
    println("Авторизация")
    val authorization = Authorization
    val session = authorization.startAuth()
  }
}