package app.operations

import app.errors.ErrorMessages.{errorMessageInputSelector, errorMessageNegativeBalance, errorMessageNegativeLimit}
import app.models._
import app.ui.Console.getInputSelector
import app.ui.Render

import scala.annotation.tailrec
import scala.io.StdIn

object OpenAccount extends Screen {
  @tailrec
  def getTypeAccount: AccountType = {
    Render.renderTypeAccount()
    getInputSelector match {
      case 1 => Debit
      case 2 => Credit
      case 3 => Savings
      case _ => {
        errorMessageInputSelector()
        getTypeAccount
      }
    }
  }

  @tailrec
  private def getBalance: Double = {
    print("Введите первоначальный баланс для счета: ")
    StdIn.readDouble() match {
      case bal if bal >= 0d => bal
      case _                => errorMessageNegativeBalance(); getBalance
    }
  }

  @tailrec
  private def getLimit: Double = {
    print("Введите лимит для счета: ")
    StdIn.readDouble() match {
      case lim if lim >= 0d => lim
      case _                => errorMessageNegativeLimit(); getLimit
    }
  }

  override def view(session: Session): Unit = {
    println("\nОТКРЫТИЕ НОВОГО СЧЕТА")
    val typeAccount = getTypeAccount
    val balance = getBalance
    typeAccount match {
      case Debit   => {
        val newDebitAccount = DebitAccount(balance)
        val accounts: Seq[DebitAccount] = AccountJsonUtil.loadDebitAccountsFromJsonFile(session.username)
        val updatedAccounts = accounts :+ newDebitAccount
        AccountJsonUtil.saveDebitAccountToJsonFile(updatedAccounts, session.username)
      }
      case Credit  => {
        val newCreditAccount = CreditAccount(balance, getLimit)
        val accounts: Seq[CreditAccount] = AccountJsonUtil.loadCreditAccountFromJsonFile(session.username)
        val updatedAccounts = accounts :+ newCreditAccount
        AccountJsonUtil.saveCreditAccountToJsonFile(updatedAccounts, session.username)
      }
      case Savings => {
        val newSavingsAccount = SavingsAccount(balance)
        val accounts: Seq[SavingsAccount] = AccountJsonUtil.loadSavingsAccountFromJsonFile(session.username)
        val updatedAccounts = accounts :+ newSavingsAccount
        AccountJsonUtil.saveSavingsAccountToJsonFile(updatedAccounts, session.username)
      }
    }

    println("Поздравляю! Счет открыт")

  }
}
