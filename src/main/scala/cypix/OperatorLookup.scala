package cypix

import java.text.ParseException

object OperatorLookup {
  def pack(request: OperatorLookupRequest): String = ???
  def unpack(payload: String): Either[ParseException, OperatorLookupResponse] = ???
}

case class OperatorLookupRequest(serviceId: String, secret: String, msisdn: String)
case class OperatorLookupResponse(operator: String)
