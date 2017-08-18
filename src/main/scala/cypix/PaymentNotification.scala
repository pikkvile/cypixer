package cypix

import java.util.Currency

import org.joda.time.DateTime
import com.netaporter.uri.Uri.parse

import Utils._

trait PaymentNotification
case class PaymentProcessed(serviceId: String, orderId: String, processingStatus: String,
                            price: BigDecimal, priceRub: BigDecimal, currency: Currency, share: BigDecimal,
                            shareRub: BigDecimal, transactionDate: DateTime, transactionId: String,
                            paymentMethod: PaymentMethod) extends PaymentNotification
case class PaymentFailed(serviceId: Option[String], orderId: Option[String], processingStatus: String, errorCode: Int,
                         price: Option[BigDecimal], priceRub: Option[BigDecimal], currency: Option[Currency],
                         share: Option[BigDecimal], shareRub: Option[BigDecimal], transactionDate: Option[DateTime],
                         transactionId: Option[String], paymentMethod: Option[PaymentMethod]) extends PaymentNotification

object PaymentNotification {

  def unpack(payload: String, secret: String): PaymentNotification = {

    val queryMap = parse(payload).query.params.toMap.withDefaultValue(None)
    val get = (key: String) => {
      queryMap(key) match {
        case Some(s) => s
        case None => throw new IllegalArgumentException(s"Can not find $key in $payload")
      }
    }
    val getOrEmpty = (key: String) => {
      queryMap(key) match {
        case Some(s) => s
        case None => ""
      }
    }

    val processingStatus = get("processing_status")
    val signature = get("hash")

    processingStatus match {

      case "PROCESSED" =>
        val calculatedSignature = hash(get("service_id") + get("transaction_id") + get("service_id") +
          processingStatus + get("price") + get("price_rub") + get("currency") + get("share") + get("share_rub") +
          get("transaction_date") + get("payment_method_id") + secret)
        if (signature == calculatedSignature)
          PaymentProcessed(get("service_id"), get("order_id"), get("processing_status"),
            BigDecimal(get("price")), BigDecimal(get("price_rub")), Currency.getInstance(get("currency")),
            BigDecimal(get("share")), BigDecimal(get("share_rub")),
            new DateTime(get("transaction_date").replace(" ", "T")),
            get("transaction_id"), PaymentMethod.of(get("payment_method_id").toInt))
        else throw new SecurityException(s"Hash check failed for $payload")

      case "FAILED" =>
        val calculatedSignature = hash(getOrEmpty("service_id") + getOrEmpty("transaction_id") +
          getOrEmpty("order_id") + processingStatus + getOrEmpty("price") + getOrEmpty("price_rub") +
          getOrEmpty("currency") + getOrEmpty("share") + getOrEmpty("share_rub") + getOrEmpty("transaction_date") +
          getOrEmpty("payment_method_id") + secret)
        if (signature == calculatedSignature)
          PaymentFailed(queryMap("service_id"), queryMap("order_id"), get("processing_status"), get("error_code").toInt,
            queryMap("price").map(s => BigDecimal(s)), queryMap("price_rub").map(s => BigDecimal(s)),
            queryMap("currency").map(Currency.getInstance),
            queryMap("share").map(s => BigDecimal(s)), queryMap("share_rub").map(s => BigDecimal(s)),
            queryMap("transaction_date").map(s => new DateTime(s.replace(" ", "T"))),
            queryMap("transaction_id"), queryMap("payment_method_id").map(s => PaymentMethod.of(s.toInt)))
        else throw new SecurityException(s"Hash check failed for $payload")

      case _ => throw new IllegalArgumentException(s"Unknown processingStatus $processingStatus")
    }
  }
}
