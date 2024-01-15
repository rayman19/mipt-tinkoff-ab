import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import app.operations.Authorization

class AuthorizationTest extends AnyFlatSpec with Matchers {
  // Тест для проверки Авторизации на случайных данных
  it should "successfully authenticate with random input" in {
    // Arrange (подготовка данных для теста)
    val authorization = Authorization

    // Act (выполнение действия, которое нужно протестировать)
    val session = authorization.startAuth()

    // Assert (проверка результата)
    session should not be null
  }

  // Другие тесты...
}