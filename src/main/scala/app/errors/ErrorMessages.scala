package app.errors

object ErrorMessages {
  def errorMessageInputSelector(): Unit = {
    println("Введите валидное число")
  }

  def errorMessageAuthInvalidPass(): Unit = {
    println("Ошибка авторизации. Неправильный пароль к логину")
    println("Повторить авторизацию? (0 - нет)")
  }

  def errorMessageAuthInvalidUsernameAndPass(): Unit = {
    println("Ошибка авторизации. Неправильный логин и пароль")
    println("Вы новый пользователь?")
    println("1. Да. Создать аккаунт")
    println("2. Нет. Ввести заново данные")
    println("0. Выйти из приложения")
  }

  def errorMessageNegativeBalance(): Unit = {
    println("Ошибка. Баланс не может быть отрицательным!")
  }

  def errorMessageNegativeLimit(): Unit = {
    println("Ошибка. Лимит не может быть отрицательным!")
  }

  def errorMessageBalanceCrossLimit(): Unit = {
    println("Ошибка. Баланс находится не в рамках лимита!")
  }
}
