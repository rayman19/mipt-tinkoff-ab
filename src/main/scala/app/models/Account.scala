package app.models

import app.config.JsonPaths.{creditAccountsPath, debitAccountsPath, savingsAccountsPath}
import play.api.libs.json.{JsValue, Json, OFormat}

import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}
import java.time.LocalDateTime

trait Account {

  def balance: Double
  def deposit(amount: Double): Account
  def withdraw(amount: Double): Either[String, Account]

}

// Дебетовый счет
case class DebitAccount(balance: Double) extends Account {

  def deposit(amount: Double): Account = {
    DebitAccount(balance + amount)
  }

  def withdraw(amount: Double): Either[String, DebitAccount] = {
    if (amount > balance) {
      Left("Недостаточно средств на счете!")
    } else {
      Right(DebitAccount(balance - amount))
    }
  }

}

// Кредитный счет
case class CreditAccount(balance: Double, limit: Double) extends Account {

  def deposit(amount: Double): Account = {
    CreditAccount(balance + amount, limit)
  }

  def withdraw(amount: Double): Either[String, CreditAccount] = {
    if (balance - amount + limit < 0) {
      Left("Операция превышает лимит!")
    } else {
      Right(CreditAccount(balance - amount, limit))
    }
  }

}

// Накопительный счет
case class SavingsAccount(balance: Double, interestRate: Double = 1d) extends Account {

  val dateLastUpdateAccount: LocalDateTime = LocalDateTime.now()

  def deposit(amount: Double): Account = {
    SavingsAccount(balance + amount, interestRate)
  }

  def withdraw(amount: Double): Either[String, SavingsAccount] = {
    if (amount > balance) {
      Left("Недостаточно средств на счете!")
    } else {
      Right(SavingsAccount(balance - amount, interestRate))
    }
  }

  def calculateInterest(): Double = {
    balance * (interestRate / 100)
  }

}

object AccountJsonUtil {

  implicit val debitAccountFormat: OFormat[DebitAccount] = Json.format[DebitAccount]
  implicit val creditAccountFormat: OFormat[CreditAccount] = Json.format[CreditAccount]
  implicit val savingsAccountFormat: OFormat[SavingsAccount] = Json.format[SavingsAccount]

  private def checkValidJson(path: String) = {
    Files.exists(Paths.get(path)) && Files.size(Paths.get(path)) > 0
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