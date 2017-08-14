package cypix

import java.text.ParseException
import java.util.Currency

import scala.math.BigDecimal.RoundingMode

object Transaction {
  def pack(request: TransactionRequest): String = ???
  def unpack(payload: String): Either[ParseException, TransactionResponse] = ???
}

case class TransactionRequest(serviceId: String, orderId: String, paymentMethodId: PaymentMethod, sum: BigDecimal,
                              currency: Currency, msisdn: Option[String] = Option.empty,
                              description: Option[String] = Option.empty) {

  sum.setScale(2, RoundingMode.HALF_UP)
}

case class TransactionResponse(processingStatus: String, errorCode: Int, location: Option[String],
                               transactionId: Option[String])
