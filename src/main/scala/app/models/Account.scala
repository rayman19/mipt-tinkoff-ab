package app.models

import app.config.JsonPaths.{creditAccountsPath, debitAccountsPath, savingsAccountsPath}
import play.api.libs.json.{JsValue, Json, OFormat}

import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}
import java.time.LocalDateTime
import java.util.UUID

trait Account {
  def id: UUID
  def balance: Double
  def deposit(amount: Double): Account
  def withdraw(amount: Double): Either[OperationStatus, Account]

}

// Дебетовый счет
case class DebitAccount(balance: Double, id: UUID = UUID.randomUUID()) extends Account {

  def deposit(amount: Double): Account = {
    DebitAccount(balance + amount, id)
  }

  def withdraw(amount: Double): Either[OperationStatus, DebitAccount] = {
    if (amount > balance) {
      Left(Failed)
    } else {
      Right(DebitAccount(balance - amount, id))
    }
  }
}

// Кредитный счет
case class CreditAccount(balance: Double, limit: Double, id: UUID = UUID.randomUUID()) extends Account {

  def deposit(amount: Double): Account = {
    CreditAccount(balance + amount, limit, id)
  }

  def withdraw(amount: Double): Either[OperationStatus, CreditAccount] = {
    if (balance - amount + limit < 0) {
      Left(Failed)
    } else {
      Right(CreditAccount(balance - amount, limit, id))
    }
  }

}

// Накопительный счет
case class SavingsAccount(balance: Double, interestRate: Double = 1d, id: UUID = UUID.randomUUID()) extends Account {

  val dateLastUpdateAccount: LocalDateTime = LocalDateTime.now()

  def deposit(amount: Double): Account = {
    SavingsAccount(balance + amount, interestRate, id)
  }

  def withdraw(amount: Double): Either[OperationStatus, SavingsAccount] = {
    if (amount > balance) {
      Left(Failed)
    } else {
      Right(SavingsAccount(balance - amount, interestRate, id))
    }
  }

  def calculateInterest(): Double = {
    balance * (interestRate / 100)
  }

}

object Account {
  def viewAccounts(accounts: Seq[Account]): Unit = {
    accounts.zipWithIndex.foreach {
      case (account, index) =>
        account match {
          case debitAccount: DebitAccount =>
            println(s"Счет №${index + 1}: Дебетовый счет - Баланс ${debitAccount.balance} руб")

          case creditAccount: CreditAccount =>
            println(s"Счет №${index + 1}: Кредитный счет - Баланс ${creditAccount.balance} руб, Лимит ${creditAccount.limit} руб")

          case savingsAccount: SavingsAccount =>
            println(s"Счет №${index + 1}: Накопительный счет - Баланс ${savingsAccount.balance} руб, Процент ${savingsAccount.interestRate}%")
        }
    }
  }

  def isEmptyListAccounts(accounts: Seq[Account]): Boolean = {
    if (accounts.isEmpty) {
      println("У вас пока нет открытых счетов")
      true
    }
    else {
      false
    }
  }
}

object AccountJsonUtil {

  implicit val debitAccountFormat: OFormat[DebitAccount] = Json.format[DebitAccount]
  implicit val creditAccountFormat: OFormat[CreditAccount] = Json.format[CreditAccount]
  implicit val savingsAccountFormat: OFormat[SavingsAccount] = Json.format[SavingsAccount]

  private def checkValidJson(path: String) = {
    Files.exists(Paths.get(path)) && Files.size(Paths.get(path)) > 0
  }

  def updateAccount(account: Account, username: String): Unit = {
    account match {
      case ac: DebitAccount => {
        val accounts = loadDebitAccountsFromJsonFile(username).filterNot(_.id == account.id)
        saveDebitAccountToJsonFile(accounts :+ ac, username)
      }
      case ac: CreditAccount => {
        val accounts = loadCreditAccountFromJsonFile(username).filterNot(_.id == account.id)
        saveCreditAccountToJsonFile(accounts :+ ac, username)
      }
      case ac: SavingsAccount => {
        val accounts = loadSavingsAccountFromJsonFile(username).filterNot(_.id == account.id)
        saveSavingsAccountToJsonFile(accounts :+ ac, username)
      }
    }
  }

  def loadDebitAccountsFromJsonFile(username: String): Seq[DebitAccount] = {
    if (checkValidJson(debitAccountsPath(username))) {
      val json = scala.io.Source.fromFile(debitAccountsPath(username)).getLines().mkString
      Json.parse(json).as[Seq[DebitAccount]]
    } else {
      Seq.empty[DebitAccount]
    }
  }

  def loadCreditAccountFromJsonFile(username: String): Seq[CreditAccount] = {
    if (checkValidJson(creditAccountsPath(username))) {
      val json = scala.io.Source.fromFile(creditAccountsPath(username)).getLines().mkString
      Json.parse(json).as[Seq[CreditAccount]]
    } else {
      Seq.empty[CreditAccount]
    }
  }

  def loadSavingsAccountFromJsonFile(username: String): Seq[SavingsAccount] = {
    if (checkValidJson(savingsAccountsPath(username))) {
      val json = scala.io.Source.fromFile(savingsAccountsPath(username)).getLines().mkString
      Json.parse(json).as[Seq[SavingsAccount]]
    } else {
      Seq.empty[SavingsAccount]
    }
  }

  def saveDebitAccountToJsonFile(accounts: Seq[DebitAccount], username: String): Unit = {
    val updatedJson: JsValue = Json.toJson(accounts)
    val updatedJsonString: String = Json.prettyPrint(updatedJson)
    val writer = new PrintWriter(new File(debitAccountsPath(username)))
    writer.write(updatedJsonString)
    writer.close()
  }

  def saveCreditAccountToJsonFile(accounts: Seq[CreditAccount], username: String): Unit = {
    val updatedJson: JsValue = Json.toJson(accounts)
    val updatedJsonString: String = Json.prettyPrint(updatedJson)
    val writer = new PrintWriter(new File(creditAccountsPath(username)))
    writer.write(updatedJsonString)
    writer.close()
  }

  def saveSavingsAccountToJsonFile(accounts: Seq[SavingsAccount], username: String): Unit = {
    val updatedJson: JsValue = Json.toJson(accounts)
    val updatedJsonString: String = Json.prettyPrint(updatedJson)
    val writer = new PrintWriter(new File(savingsAccountsPath(username)))
    writer.write(updatedJsonString)
    writer.close()
  }

  def createEmptyJson(path: String): Unit = {
    val file = new File(path)
    if (!file.exists()) {
      file.createNewFile()
    }
  }
}