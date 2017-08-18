package cypix

import java.util.Currency

import org.joda.time.DateTime
import com.netaporter.uri.Uri.parse

import Utils._

case class PaymentNotification(serviceId: String, orderId: String, processingStatus: String, errorCode: Option[Int],
                               price: BigDecimal, priceRub: BigDecimal, currency: Currency, share: BigDecimal,
                               shareRub: BigDecimal, transactionDate: DateTime, transactionId: String,
                               paymentMethod: PaymentMethod)
object PaymentNotification {

  def unpack(payload: String, secret: String): PaymentNotification = {
    val queryMap = parse(payload).query.params.toMap
    val get = (key: String) => {
      queryMap(key) match {
        case Some(s) => s
        case None => throw new IllegalArgumentException(s"Can not find $key in $payload")
      }
    }
    val processingStatus = get("processing_status")
    val signature = get("hash")
    val calculatedSignature = hash(get("service_id") + get("transaction_id") + get("service_id") +
      processingStatus + get("price") + get("price_rub") + get("currency") + get("share") + get("share_rub") +
      get("transaction_date") + get("payment_method_id") + secret)
    val errorCode = if (processingStatus == "FAILED") Some(get("error_code").toInt) else None
    if (signature == calculatedSignature)
      PaymentNotification(get("service_id"), get("order_id"), get("processing_status"), errorCode,
                          BigDecimal(get("price")), BigDecimal(get("price_rub")), Currency.getInstance(get("currency")),
                          BigDecimal(get("share")), BigDecimal(get("share_rub")),
                          new DateTime(get("transaction_date").replace(" ", "T")),
                          get("transaction_id"), PaymentMethod.of(get("payment_method_id").toInt))
    else throw new SecurityException(s"Hash check failed for $payload")
  }
}
