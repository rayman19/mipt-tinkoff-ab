package app.models

import play.api.libs.json.{JsValue, Json, OFormat}
import java.io.{File, PrintWriter}

sealed trait AuthStatus
case object Valid extends AuthStatus
case object Invalid extends AuthStatus
case object New extends AuthStatus

case class User(username: String, password: String)

object UserJsonUtil {
  implicit val userFormat: OFormat[User] = Json.format[User]

  def loadUsersFromJsonFile(usersPath: String): Seq[User] = {
    val json = scala.io.Source.fromFile(usersPath).getLines().mkString
    Json.parse(json).as[Seq[User]]
  }

  def saveUsersToJsonFile(users: Seq[User], usersPath: String): Unit = {
    val updatedJson: JsValue = Json.toJson(users)
    val updatedJsonString: String = Json.prettyPrint(updatedJson)
    val writer = new PrintWriter(new File(usersPath))
    writer.write(updatedJsonString)
    writer.close()
  }
}
