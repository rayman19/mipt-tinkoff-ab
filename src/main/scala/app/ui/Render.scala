package app.ui

object Render {
  def renderMainMenu(): Unit = {
    println("ГЛАВНОЕ МЕНЮ")
    println("1. Просмотр счетов")
    println("2. Открыть счет")
    println("3. Закрыть счет")
    println("4. Сформировать отчет")
    println("0. Выход")
  }
}