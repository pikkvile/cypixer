package cypix

trait PaymentMethod {def id: Int}
case object MOBILE extends PaymentMethod {val id = 1}
case object BANK_CARD extends PaymentMethod {val id = 2}
object PaymentMethod {
  def of(id: Int): PaymentMethod = id match {
    case 1 => MOBILE
    case 2 => BANK_CARD
  }
}



