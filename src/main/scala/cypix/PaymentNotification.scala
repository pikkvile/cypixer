package cypix

import java.util.Currency

import org.joda.time.DateTime
import com.netaporter.uri.Uri.parse

case class PaymentNotification(serviceId: String, orderId: String, processingStatus: String, errorCode: Int,
                               price: BigDecimal, priceRub: BigDecimal, currency: Currency, share: BigDecimal,
                               shareRub: BigDecimal, transactionDate: DateTime, paymentMethod: PaymentMethod)
object PaymentNotification {

  def unpack(payload: String): PaymentNotification = {
    val queryMap = parse(payload).query.params.toMap
    val get = (key: String) => {
      queryMap(key) match {
        case Some(s) => s
        case None => throw new IllegalArgumentException(s"Can not find $key in $payload")
      }
    }
    PaymentNotification(get("service_id"), get("orderId"), get("processingStatus"), get("errorCode").toInt,
                        BigDecimal(get("price")), BigDecimal(get("priceRub")), Currency.getInstance(get("currency")),
                        BigDecimal(get("share")), BigDecimal(get("shareRub")), new DateTime(get("transactionDate")),
                        PaymentMethod.of(get("paymentMethod").toInt))
  }
}
