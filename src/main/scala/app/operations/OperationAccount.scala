package app.operations
import app.config.JsonPaths.operationsPath
import app.models.Account.{isEmptyListAccounts, viewAccounts}
import app.models.AccountJsonUtil.updateAccount
import app.models.Operation.{inputAmount, inputNameOperation, selectOperation}
import app.models._
import app.ui.Console.getInputSelector

import java.time.LocalDateTime
import scala.annotation.tailrec

object OperationAccount extends Screen {
  @tailrec
  def getAccount(accounts: Seq[Account]): Option[Account] = {
    getInputSelector match {
      case 0                        => None
      case n if (n > accounts.size) => getAccount(accounts)
      case n                        => Some(accounts(n - 1))
    }
  }

  def operationAccount(account: Account, session: Session): Operation = {
    val typeOperation = selectOperation
    val name = inputNameOperation
    val amount = inputAmount
    typeOperation match {
      case Deposit  => {
        val deposit = account.deposit(amount)
        updateAccount(deposit, session.username)
        Operation(name = name,
          date = LocalDateTime.now(),
          amount = amount,
          idAccount = account.id,
          operationType = typeOperation,
          operationStatus = Success)
      }
      case Withdraw => {
        val withdraw = account.withdraw(amount)
        withdraw match {
          case Left(_) => Operation(name = name,
            date = LocalDateTime.now(),
            amount = amount,
            idAccount = account.id,
            operationType = typeOperation,
            operationStatus = Failed)
          case Right(value) => {
            updateAccount(value, session.username)
            Operation(name = name,
              date = LocalDateTime.now(),
              amount = amount,
              idAccount = account.id,
              operationType = typeOperation,
              operationStatus = Success)
          }
        }
      }
    }
  }

  private def saveOperation(operation: Operation, session: Session): Unit = {
    import app.models.OperationJsonUtil._
    val operations = OperationJsonUtil.loadOperationFromJsonFile(operationsPath(session.username))
    val updateOperations = operations :+ operation
    saveOperationToJsonFile(updateOperations, operationsPath(session.username))
  }

  override def view(session: Session): Unit = {
    val debitAccount: Seq[DebitAccount] = AccountJsonUtil.loadDebitAccountsFromJsonFile(session.username)
    val creditAccount: Seq[CreditAccount] = AccountJsonUtil.loadCreditAccountFromJsonFile(session.username)
    val savingsAccount: Seq[SavingsAccount] = AccountJsonUtil.loadSavingsAccountFromJsonFile(session.username)
    val accounts = debitAccount ++ creditAccount ++ savingsAccount

    if (!isEmptyListAccounts(accounts)) {
      println("\nВАШИ СЧЕТА")
      viewAccounts(accounts)
      println("0. Выход")
      getAccount(accounts) match {
        case Some(account) => saveOperation(operationAccount(account, session), session)
        case None          => ()
      }
    }
  }
}
