package sha256

import scala.scalanative.libc.stdlib
import scala.scalanative.libc.string
import scala.scalanative.unsafe._
import scala.scalanative.unsigned._

object Hmac512 {
  def hmac(
      key: Array[UByte],
      msg: Array[UByte]
  ): Array[UByte] = {
    Zone { implicit z =>
      {
        val keySize = key.size.toLong.toULong
        val keyData = alloc[UByte](keySize).asInstanceOf[Ptr[UByte]]
        for (i <- 0 until key.size) {
          !(keyData + i) = key(i)
        }

        val msgSize = msg.size.toLong.toULong
        val msgData = alloc[UByte](msgSize).asInstanceOf[Ptr[UByte]]
        for (i <- 0 until msg.size) {
          !(msgData + i) = msg(i)
        }

        val result = alloc[UByte](64).asInstanceOf[Ptr[UByte]]
        Hmac512Extern.hmac_sha512(
          result,
          keyData,
          keySize,
          msgData,
          msgSize
        )

        val res = Array.ofDim[UByte](64)
        for (i <- 0 until 64) {
          res(i) = (!(result + i)).toUByte
        }
        res
      }
    }
  }
}

@extern
object Hmac512Extern {
  def hmac_sha512(
      hmac: Ptr[UByte],
      k: Ptr[UByte],
      ksize: CSize,
      d: Ptr[UByte],
      dsize: CSize
  ): Unit = extern
}
