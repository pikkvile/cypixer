package cypix

import org.scalatest.{FlatSpec, Matchers}

class OperatorLookupSpec extends FlatSpec with Matchers {

  "OperatorLookupRequest" should "be correctly packed" in {
    val operatorLookupRequest = OperatorLookupRequest("12345", "sdfxcv", "79504567182")
    operatorLookupRequest.pack() should be ("https://api.cypix.ru/operator/?service_id=12345&secret=sdfxcv&msisdn=79504567182")
  }

  "OperatorLookupResponse" should "be parsed" in {
    val payload = """{
        "operator_name": "beeline"
    }"""
    val response = OperatorLookupResponse.unpack(payload)
    response.operatorName should be ("beeline")
  }
}
