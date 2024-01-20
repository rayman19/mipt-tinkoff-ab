package app.operations

import app.config.JsonPaths.operationsPath
import app.models.OperationJsonUtil.loadOperationFromJsonFile
import app.models.{Deposit, Failed, Session, Success, Withdraw}
import app.ui.Console.getAnyButton

object GenerateReport extends Screen {
  override def view(session: Session): Unit = {
    println("\nИСТОРИЯ ОПЕРАЦИЙ")
    loadOperationFromJsonFile(operationsPath(session.username))
      .groupBy(_.idAccount)
      .foreach { case (id, ops) =>
        println(s"ID: $id")
        ops.foreach { operation => {
            println()
            println(s"${operation.date} <${operation.name}>")
            operation.operationType match {
              case Deposit  => println("Операция: Пополнения счета")
              case Withdraw => println("Операция: Снятия со счета")
            }
            println(s"Сумма: ${operation.amount}")
            operation.operationStatus match {
              case Success => println("Статус: Выполнено")
              case Failed  => println("Статус: Отмена")
            }
          }
        }
        println()
      }
    getAnyButton
  }
}
