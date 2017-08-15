package cypix

import java.text.ParseException
import java.util.Currency

import scala.math.BigDecimal.RoundingMode

import Utils._

case class TransactionRequest(secret: String, serviceId: String, orderId: String,
                              paymentMethod: PaymentMethod, sum: BigDecimal,
                              currency: Currency = Currency.getInstance("RUB"),
                              msisdn: Option[String] = None, description: Option[String] = None) {

  private val sumRounded = sum.setScale(2, RoundingMode.HALF_UP)
  def pack(): String = {
      val q = serviceId + orderId + paymentMethod.id + sumRounded + currency + msisdn.getOrElse("") +
              description.getOrElse("") + secret
      s"https://api.cypix.ru/transaction/?service_id=$serviceId&order_id=$orderId&summ=$sumRounded&currency=$currency" +
        s"&payment_method_id=${paymentMethod.id}" +
        msisdn.map(m => s"&msisdn=$m").getOrElse("") +
        description.map(d => s"&description=$d").getOrElse("") +
        s"&hash=${hash(q)}"
  }
}

case class TransactionResponse(processingStatus: String, errorCode: Int, location: Option[String],
                               transactionId: Option[String])
object TransactionResponse {
  def unpack(payload: String): Either[ParseException, TransactionResponse] = ???
}