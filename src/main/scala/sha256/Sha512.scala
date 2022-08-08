package sha256

import scala.scalanative.libc.stdlib
import scala.scalanative.libc.string
import scala.scalanative.unsafe._
import scala.scalanative.unsigned._

object Sha512 {
  def sha512(input: String): Array[UByte] = {
    Zone { implicit z =>
      {
        val str = toCString(input)
        val size: CSize = string.strlen(str)
        val data = alloc[Byte](size).asInstanceOf[Ptr[Byte]]
        string.strncpy(data, str, size)
        sha512(data.asInstanceOf[Ptr[UByte]], size)
      }
    }
  }

  def sha512(bytearray: Array[UByte]): Array[UByte] = {
    Zone { implicit z =>
      {
        val size = bytearray.size.toLong.toULong
        val data = alloc[UByte](size).asInstanceOf[Ptr[UByte]]
        for (i <- 0 until bytearray.size) {
          !(data + i) = bytearray(i)
        }
        sha512(data, size)
      }
    }
  }

  def sha512(payload: Ptr[UByte], size: CSize): Array[UByte] = {
    val hash = stdlib.malloc(64L.toULong).asInstanceOf[Ptr[UByte]]
    Sha512Extern.sha512(hash, payload, size);
    val res = Array.ofDim[UByte](64)
    for (i <- 0 until 64) {
      res(i) = (!(hash + i)).toUByte
    }
    stdlib.free(hash.asInstanceOf[Ptr[Byte]])
    res
  }
}

@extern
object Sha512Extern {
  def sha512(hash: Ptr[UByte], payload: Ptr[UByte], len: CSize): Unit = extern
}
