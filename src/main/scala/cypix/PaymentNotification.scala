package cypix

import java.text.ParseException
import java.util.Currency

import org.joda.time.DateTime

case class PaymentNotification(serviceId: String, orderId: String, processingStatus: String, errorCode: Int,
                               price: BigDecimal, priceRub: BigDecimal, currency: Currency, share: BigDecimal,
                               shareRub: BigDecimal, transactionDate: DateTime, paymentMethod: PaymentMethod)
object PaymentNotification {
  def unpack(payload: String): Either[ParseException, PaymentNotification] = ???
}
