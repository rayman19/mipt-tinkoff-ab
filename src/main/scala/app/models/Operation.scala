package app.models

import java.util.Date

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

case class Operation(date: Date,
                     amount: Double,
                     operationType: OperationType,
                     operationStatus: OperationStatus)

