package sha256

import scala.scalanative.libc.stdlib
import scala.scalanative.libc.string
import scala.scalanative.unsafe._
import scala.scalanative.unsigned._

object Sha256 {
  def sha256(input: String): Array[UByte] = {
    Zone { implicit z =>
      {
        val str = toCString(input)
        val size: CSize = string.strlen(str)
        val data = alloc[Byte](size).asInstanceOf[Ptr[Byte]]
        string.strncpy(data, str, size)
        sha256(data, size)
      }
    }
  }

  def sha256(bytearray: Array[Byte]): Array[UByte] = {
    Zone { implicit z =>
      {
        val size = bytearray.size.toLong.toULong
        val data = alloc[Byte](size).asInstanceOf[Ptr[Byte]]
        for (i <- 0 until bytearray.size) {
          !(data + i) = bytearray(i)
        }
        sha256(data, size)
      }
    }
  }

  def sha256(data: Ptr[Byte], size: CSize): Array[UByte] = {
    val hash = stdlib.malloc(32L.toULong).asInstanceOf[Ptr[UByte]]
    Sha256Extern.sha256(data, size, hash);
    val res = Array.ofDim[UByte](32)
    for (i <- 0 until 32) {
      res(i) = !(hash + i)
    }
    stdlib.free(hash.asInstanceOf[Ptr[Byte]])
    res
  }
}

@extern
object Sha256Extern {
  def sha256(data: Ptr[Byte], len: CSize, hash: Ptr[UByte]): Unit = extern
}
