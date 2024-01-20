import app.config.JsonPaths.usersPath
import app.config.JsonPathsTests
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import app.operations.Authorization
import app.models.{Invalid, New, User, UserJsonUtil, Valid}

class AuthorizationTest extends AnyFlatSpec with Matchers {
  val users: Seq[User] = UserJsonUtil.loadUsersFromJsonFile(JsonPathsTests.usersPath)

  it should "return an valid for an valid username and password" in {
    val validUsername = "root"
    val validPassword = "pass"

    val checkValid = Authorization.checkValidFromJson(users, validUsername, validPassword)

    checkValid shouldEqual Valid
  }

  it should "return an invalid for an valid username but invalid password" in {
    val validUsername = "root"
    val invalidPassword = "qwerty"

    val checkValid = Authorization.checkValidFromJson(users, validUsername, invalidPassword)

    checkValid shouldEqual Invalid
  }

  it should "return an new for an invalid username and password" in {
    val newUsername = "new_root"
    val newPassword = "mew_pass"

    val checkValid = Authorization.checkValidFromJson(users, newUsername, newPassword)

    checkValid shouldEqual New
  }
}