package app.operations

import app.models.Session

trait Screen {
  def view(session: Session): Unit
}
