package cypix

import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods.parse

case class OperatorLookupRequest(serviceId: String, secret: String, msisdn: String) {
  def pack(request: OperatorLookupRequest): String =
    s"https://api.cypix.ru/operator/?service_id=$serviceId&secret=$secret&msisdn=$msisdn"
}

case class OperatorLookupResponse(operator: String)
object OperatorLookupResponse {
  implicit val formats = DefaultFormats
  def unpack(payload: String): OperatorLookupResponse = {
    parse(payload).camelizeKeys.extract[OperatorLookupResponse]
  }
}