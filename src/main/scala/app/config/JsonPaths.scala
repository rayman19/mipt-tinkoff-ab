package app.config

import java.nio.file.{Path, Paths}

object JsonPaths {
  val basePath: String =
    Paths.get("data").toAbsolutePath.toString
  def usersPath: String =
    Paths.get(basePath, "users.json").toString

  def operationsPath(username: String): String =
    Paths.get(basePath, username, "history.json").toString

  def usernamePath(username: String): String =
    Paths.get(basePath, username).toString
  def debitAccountsPath(username: String): String =
    Paths.get(usernamePath(username), "debitAccounts.json").toString
  def creditAccountsPath(username: String): String =
    Paths.get(usernamePath(username), "creditAccounts.json").toString
  def savingsAccountsPath(username: String): String =
    Paths.get(usernamePath(username), "savingsAccounts.json").toString
}
