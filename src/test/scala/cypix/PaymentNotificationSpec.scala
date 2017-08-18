package cypix

import java.util.Currency

import org.joda.time.DateTime
import org.scalatest.{FlatSpec, Matchers}

class PaymentNotificationSpec extends FlatSpec with Matchers {

  "PaymentNotification" should "be parsed" in {
    val request = "http://some.ru/cypix/?service_id=5432&order_id=12345678asdfgh&processing_status=PROCESSED" +
      "&price=120.20&price_rub=120.20&currency=RUB&share=120.20&share_rub=120.20&transaction_date=2016-03-08 00:51:52" +
      "&transaction_id=2016030&payment_method_id=2&hash=4804824075c79f3ef22fab36a68eb566"
    val unpacked = PaymentNotification.unpack(request, "sdfsdf")
    unpacked.serviceId should be ("5432")
    unpacked.orderId should be ("12345678asdfgh")
    unpacked.processingStatus should be ("PROCESSED")
    unpacked.price should be (BigDecimal(120.20))
    unpacked.priceRub should be (BigDecimal(120.20))
    unpacked.share should be (BigDecimal(120.20))
    unpacked.shareRub should be (BigDecimal(120.20))
    unpacked.currency should be (Currency.getInstance("RUB"))
    unpacked.transactionDate should be (new DateTime("2016-03-08T00:51:52"))
    unpacked.transactionId should be ("2016030")
    unpacked.paymentMethod should be (BANK_CARD)
    unpacked.errorCode should be (None)
  }

  "SecurityException" should "be thrown" in {
    assertThrows[SecurityException] {
      val request = "http://some.ru/cypix/?service_id=5432&order_id=12345678asdfgh&processing_status=PROCESSED" +
        "&price=120.20&price_rub=120.20&currency=RUB&share=120.20&share_rub=120.20&transaction_date=2016-03-08 00:51:52" +
        "&transaction_id=2016030&payment_method_id=2&hash=4804824075c79f3ef22fab36a68eb567"
      val unpacked = PaymentNotification.unpack(request, "sdfsdf")
    }
  }
}
