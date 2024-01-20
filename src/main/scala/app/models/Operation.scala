package app.models

import app.errors.ErrorMessages.errorMessageInputSelector
import app.ui.Console.getInputSelector
import app.ui.Render
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import play.api.libs.json.{Format, JsError, JsPath, JsString, JsSuccess, JsValue, Json, OFormat, Reads, Writes}

import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}
import java.time.LocalDateTime
import java.util.UUID
import scala.annotation.tailrec
import scala.io.StdIn

sealed trait AccountType
case object Debit extends AccountType
case object Credit extends AccountType
case object Savings extends AccountType

sealed trait OperationType
case object Deposit extends OperationType
case object Withdraw extends OperationType

sealed trait OperationStatus
case object Success extends OperationStatus
case object Failed extends OperationStatus

case class Operation(name: String,
                     date: LocalDateTime,
                     amount: Double,
                     idAccount: UUID,
                     operationType: OperationType,
                     operationStatus: OperationStatus)

object Operation {
  @tailrec
  def selectOperation: OperationType = {
    Render.renderTypeOperation()
    getInputSelector match {
      case 1 => Deposit
      case 2 => Withdraw
      case _ => errorMessageInputSelector(); selectOperation
    }
  }

  def inputAmount: Double = {
    println("Введите сумму: ")
    StdIn.readDouble()
  }

  def inputNameOperation: String = {
    println("Введите название операции: ")
    StdIn.readLine()
  }
}

object OperationJsonUtil {

  implicit val accountTypeFormat: Format[AccountType] = Format(
    Reads[AccountType] {
      case JsString("Debit") => JsSuccess(Debit)
      case JsString("Credit") => JsSuccess(Credit)
      case JsString("Savings") => JsSuccess(Savings)
      case _ => JsError("Invalid AccountType")
    },
    Writes[AccountType] {
      case Debit => JsString("Debit")
      case Credit => JsString("Credit")
      case Savings => JsString("Savings")
    }
  )

  implicit val operationTypeFormat: Format[OperationType] = Format(
    Reads[OperationType] {
      case JsString("Deposit") => JsSuccess(Deposit)
      case JsString("Withdraw") => JsSuccess(Withdraw)
      case _ => JsError("Invalid OperationType")
    },
    Writes[OperationType] {
      case Deposit => JsString("Deposit")
      case Withdraw => JsString("Withdraw")
    }
  )

  implicit val operationStatusFormat: Format[OperationStatus] = Format(
    Reads[OperationStatus] {
      case JsString("Success") => JsSuccess(Success)
      case JsString("Failed") => JsSuccess(Failed)
      case _ => JsError("Invalid OperationStatus")
    },
    Writes[OperationStatus] {
      case Success => JsString("Success")
      case Failed => JsString("Failed")
    }
  )

  implicit val operationFormat: Format[Operation] = (
    (JsPath \ "name").format[String] and
      (JsPath \ "date").format[LocalDateTime] and
      (JsPath \ "amount").format[Double] and
      (JsPath \ "idAccount").format[UUID] and
      (JsPath \ "operationType").format[OperationType] and
      (JsPath \ "operationStatus").format[OperationStatus]
    )(Operation.apply, unlift(Operation.unapply))

  def loadOperationFromJsonFile(operationsPath: String): Seq[Operation] = {
    if (checkValidJson(operationsPath)) {
      val json = scala.io.Source.fromFile(operationsPath).getLines().mkString
      Json.parse(json).as[Seq[Operation]]
    } else {
      Seq.empty[Operation]
    }
  }

  private def checkValidJson(operationsPath: String) = {
    Files.exists(Paths.get(operationsPath)) && Files.size(Paths.get(operationsPath)) > 0
  }

  def saveOperationToJsonFile(operations: Seq[Operation], operationsPath: String): Unit = {
    val updatedJson: JsValue = Json.toJson(operations)
    val updatedJsonString: String = Json.prettyPrint(updatedJson)
    val writer = new PrintWriter(new File(operationsPath))
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