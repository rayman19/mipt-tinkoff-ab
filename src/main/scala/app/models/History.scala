package app.models

case class History(finalDeposit: Double,
                   finalDraw: Double,
                   operations: List[Operation])