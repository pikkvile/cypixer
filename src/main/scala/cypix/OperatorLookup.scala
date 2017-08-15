package cypix

import java.text.ParseException

case class OperatorLookupRequest(serviceId: String, secret: String, msisdn: String) {
  def pack(request: OperatorLookupRequest): String = {
    s"https://api.cypix.ru/operator/?service_id=$serviceId&secret=$secret&msisdn=$msisdn"
  }
}

case class OperatorLookupResponse(operator: String)
object OperatorLookupResponse {
  def unpack(payload: String): Either[ParseException, OperatorLookupResponse] = ???
}