package app.operations

import app.models.Account.{isEmptyListAccounts, viewAccounts}
import app.models.{AccountJsonUtil, Credit, CreditAccount, Debit, DebitAccount, Savings, SavingsAccount, Session}
import app.operations.OpenAccount.{getLimit, getTypeAccount}
import app.ui.Console.getAnyButton

object ViewAccounts extends Screen {
  override def view(session: Session): Unit = {
    val debitAccount: Seq[DebitAccount] = AccountJsonUtil.loadDebitAccountsFromJsonFile(session.username)
    val creditAccount: Seq[CreditAccount] = AccountJsonUtil.loadCreditAccountFromJsonFile(session.username)
    val savingsAccount: Seq[SavingsAccount] = AccountJsonUtil.loadSavingsAccountFromJsonFile(session.username)
    val accounts = debitAccount ++ creditAccount ++ savingsAccount
    if (!isEmptyListAccounts(accounts)) {
      println("\nВАШИ СЧЕТА")
      viewAccounts(accounts)
    }
    getAnyButton
  }
}
