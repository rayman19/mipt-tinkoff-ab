package app.operations

import app.errors.ErrorMessages.errorMessageInputSelector
import app.models.Session
import app.ui.Render.renderMainMenu
import app.ui.Console.{exit, getInputSelector}

import scala.annotation.tailrec

object AccountService extends Screen {
  def view(session: Session): Unit = {
    renderMainMenu()
    getInputSelector match {
      case 1 => ViewAccounts.view(session)
      case 2 => OpenAccount.view(session)
      case 3 => OperationAccount.view(session)
      case 4 => GenerateReport.view(session)
      case 0 => exit()
      case _   => {
        errorMessageInputSelector()
        this.view(session)
      }
    }
    this.view(session)
  }
}
