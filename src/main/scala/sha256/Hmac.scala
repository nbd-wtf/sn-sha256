package sha256

import scala.scalanative.libc.stdlib
import scala.scalanative.libc.string
import scala.scalanative.unsafe._
import scala.scalanative.unsigned._

object Hmac {
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

        val result = alloc[UByte](32).asInstanceOf[Ptr[UByte]]
        HmacExtern.hmac_sha256(
          result,
          keyData,
          keySize,
          msgData,
          msgSize
        )

        val res = Array.ofDim[UByte](32)
        for (i <- 0 until 32) {
          res(i) = (!(result + i)).toUByte
        }
        res
      }
    }
  }
}

@extern
object HmacExtern {
  def hmac_sha256(
      hmac: Ptr[UByte],
      k: Ptr[UByte],
      ksize: CSize,
      d: Ptr[UByte],
      dsize: CSize
  ): Unit = extern
}
