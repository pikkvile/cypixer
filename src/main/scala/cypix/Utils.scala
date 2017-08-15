package cypix

import java.security.MessageDigest

object Utils {

  def hash(s: String): String = {
    val md = MessageDigest.getInstance("MD5")
    val bytes = s.getBytes("UTF-8")
    md.update(bytes, 0, bytes.length)
    new java.math.BigInteger(1, md.digest()).toString(16)
  }

  implicit def str2opt(s: String): Option[String] = Some(s)
}
