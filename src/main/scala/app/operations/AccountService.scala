package app.operations

import app.errors.ErrorMessages.errorMessageInputSelector
import app.ui.Render.renderMainMenu
import app.ui.Console.getInputSelector

import scala.annotation.tailrec

object AccountService extends Screen {
  @tailrec
  def view(): Unit = {
    renderMainMenu()
    getInputSelector match {
      case "1" => ViewAccounts.view()
      case "2" => CreateAccount.view()
      case "3" => CloseAccount.view()
      case "4" => GenerateReport.view()
      case "0" => ()
      case _   => {
        errorMessageInputSelector()
        this.view()
      }
    }
  }
}
