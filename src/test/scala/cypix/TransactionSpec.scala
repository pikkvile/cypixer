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

  "TransactionResponse" should "be parsed" in {
    val payload1 = """{
      "transaction_id": "123456",
      "processing_status": "ACCEPTED",
      "error_code": "0"
    }"""
    val response1 = TransactionResponse.unpack(payload1)
    response1.transactionId should be (Some("123456"))
    response1.processingStatus should be ("ACCEPTED")
    response1.errorCode should be ("0")
    response1.location should be (None)
    val payload2 = """{
      "processing_status": "DENIED",
      "error_code": "1"
    }"""
    val response2 = TransactionResponse.unpack(payload2)
    response2.transactionId should be (None)
    response2.processingStatus should be ("DENIED")
    response2.errorCode should be ("1")
    response2.location should be (None)
  }
}
