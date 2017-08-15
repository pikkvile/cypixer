package cypix

import java.util.Currency

import org.scalatest.{FlatSpec, Matchers}

import Utils._

class TransactionSpec extends FlatSpec with Matchers {

  private val usd = Currency.getInstance("USD")

  "Transaction requests" should "be correctly packed" in {
    val tr1 = TransactionRequest("kfjhgdkjfhgkdjfhg", "srv123", "ord456", MOBILE, 15.26, usd, "79503654879")
    val tr2 = TransactionRequest("kfjhgdkjfhgkdjfhg", "srv456", "ord789", MOBILE, 15.2687)
    tr1.pack() should be ("https://api.cypix.ru/transaction/?service_id=srv123&order_id=ord456&summ=15.26&" +
      "currency=USD&payment_method_id=1&msisdn=79503654879&hash=8db5b87b6446cd9321d90d3c7ad7241f")
    tr2.pack() should be ("https://api.cypix.ru/transaction/?service_id=srv456&order_id=ord789&summ=15.27&" +
      "currency=RUB&payment_method_id=1&hash=6e1460df6500647743a294cc70920fde")
  }
}
