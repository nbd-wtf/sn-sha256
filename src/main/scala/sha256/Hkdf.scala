package sha256

import scala.scalanative.libc.stdlib
import scala.scalanative.libc.string
import scala.scalanative.unsafe._
import scala.scalanative.unsigned._

object Hkdf {
  def hkdf(
      salt: Array[UByte],
      secret: Array[UByte],
      info: Array[UByte],
      n: Int
  ): Array[UByte] = {
    Zone { implicit z =>
      {
        val saltSize = salt.size.toLong.toULong
        val saltData = alloc[UByte](saltSize).asInstanceOf[Ptr[UByte]]
        for (i <- 0 until salt.size) {
          !(saltData + i) = salt(i)
        }

        val secretSize = secret.size.toLong.toULong
        val secretData = alloc[UByte](secretSize).asInstanceOf[Ptr[UByte]]
        for (i <- 0 until secret.size) {
          !(secretData + i) = secret(i)
        }

        val infoSize = info.size.toLong.toULong
        val infoData = alloc[UByte](infoSize).asInstanceOf[Ptr[UByte]]
        for (i <- 0 until info.size) {
          !(infoData + i) = info(i)
        }

        val result = alloc[UByte](n.toLong.toULong).asInstanceOf[Ptr[UByte]]
        HkdfExtern.hkdf_sha256(
          result,
          n.toLong.toULong,
          saltData,
          saltSize,
          secretData,
          secretSize,
          infoData,
          infoSize
        )

        val res = Array.ofDim[UByte](n)
        for (i <- 0 until n) {
          res(i) = (!(result + i)).toUByte
        }
        res
      }
    }
  }
}

@extern
object HkdfExtern {
  def hkdf_sha256(
      okm: Ptr[UByte],
      okm_size: CSize,
      s: Ptr[UByte],
      ssize: CSize,
      k: Ptr[UByte],
      ksize: CSize,
      info: Ptr[UByte],
      isize: CSize
  ): Unit = extern
}
