package cypix

import java.text.ParseException
import java.util.Currency

import scala.math.BigDecimal.RoundingMode

import Hasher._

case class TransactionRequest(url: String, secret: String, serviceId: String, orderId: String,
                              paymentMethod: PaymentMethod, sum: BigDecimal, currency: Currency,
                              msisdn: Option[String] = Option.empty, description: Option[String] = Option.empty) {

  sum.setScale(2, RoundingMode.HALF_UP)
  def pack(): String = {
      val q = serviceId + orderId + paymentMethod.id + sum + currency + msisdn.getOrElse("") +
        description.getOrElse("") + secret
      s"https://api.cypix.ru/transaction/?service_id=$serviceId&order_id=$orderId&summ=$sum&currency=$currency" +
        s"&payment_method_id=${paymentMethod.id}" +
        msisdn.map(_ => s"&msisdn=${_}").getOrElse("") +
        description.map(_ => s"&description=${_}").getOrElse("") +
        s"&hash=${hash(q)}"
  }
}

case class TransactionResponse(processingStatus: String, errorCode: Int, location: Option[String],
                               transactionId: Option[String])
object TransactionResponse {
  def unpack(payload: String): Either[ParseException, TransactionResponse] = ???
}