package cypix

import java.security.MessageDigest

object Hasher {
  def hash(s: String): String = {
    new String(MessageDigest.getInstance("MD5").digest(s.getBytes))
  }
}
