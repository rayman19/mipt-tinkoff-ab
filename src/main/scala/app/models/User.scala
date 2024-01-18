package app.models

import play.api.libs.json.{JsValue, Json, OFormat}

import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}

sealed trait AuthStatus
case object Valid extends AuthStatus
case object Invalid extends AuthStatus
case object New extends AuthStatus

case class User(username: String, password: String)

object UserJsonUtil {

  implicit val userFormat: OFormat[User] = Json.format[User]

  def loadUsersFromJsonFile(usersPath: String): Seq[User] = {
    if (checkValidJson(usersPath)) {
      val json = scala.io.Source.fromFile(usersPath).getLines().mkString
      Json.parse(json).as[Seq[User]]
    } else {
      Seq.empty[User]
    }
  }

  private def checkValidJson(usersPath: String) = {
    Files.exists(Paths.get(usersPath)) && Files.size(Paths.get(usersPath)) > 0
  }

  def saveUsersToJsonFile(users: Seq[User], usersPath: String): Unit = {
    val updatedJson: JsValue = Json.toJson(users)
    val updatedJsonString: String = Json.prettyPrint(updatedJson)
    val writer = new PrintWriter(new File(usersPath))
    writer.write(updatedJsonString)
    writer.close()
  }

  def createEmptyJson(path: String): Unit = {
    val file = new File(path)
    if (!file.exists()) {
      file.createNewFile()
    }
  }

  def mkdirUsername(path: String): Unit = {
    val file = new File(path)
    if (!file.exists()) {
      file.mkdir()
    }
  }

}
