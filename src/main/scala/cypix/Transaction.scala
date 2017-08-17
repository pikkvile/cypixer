package cypix

import java.util.Currency

import scala.math.BigDecimal.RoundingMode
import org.json4s.native.JsonMethods.parse
import Utils._
import org.json4s.DefaultFormats

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

case class TransactionResponse(processingStatus: String, errorCode: String, location: Option[String],
                               transactionId: Option[String])
object TransactionResponse {
  implicit val formats = DefaultFormats
  def unpack(payload: String): TransactionResponse =
    parse(payload).camelizeKeys.extract[TransactionResponse]
}