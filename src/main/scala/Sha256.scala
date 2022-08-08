import scala.scalanative.libc.stdlib
import scala.scalanative.libc.string
import scala.scalanative.unsafe._
import scala.scalanative.unsigned._

package object sha256 {
  def sha256(input: String): Array[UByte] = {
    Zone { implicit z =>
      {
        val str = toCString(input)
        val size: CSize = string.strlen(str)
        val data = alloc[Byte](size).asInstanceOf[Ptr[Byte]]
        string.strncpy(data, str, size)
        sha256(data.asInstanceOf[Ptr[UByte]], size)
      }
    }
  }

  def sha256(bytearray: Array[UByte]): Array[UByte] = {
    Zone { implicit z =>
      {
        val size = bytearray.size.toLong.toULong
        val data = alloc[UByte](size).asInstanceOf[Ptr[UByte]]
        for (i <- 0 until bytearray.size) {
          !(data + i) = bytearray(i)
        }
        sha256(data, size)
      }
    }
  }

  def sha256(payload: Ptr[UByte], size: CSize): Array[UByte] = {
    val hash = stdlib.malloc(32L.toULong).asInstanceOf[Ptr[UByte]]
    Sha256Extern.sha256(hash, payload, size);
    val res = Array.ofDim[UByte](32)
    for (i <- 0 until 32) {
      res(i) = (!(hash + i)).toUByte
    }
    stdlib.free(hash.asInstanceOf[Ptr[Byte]])
    res
  }

  @extern
  object Sha256Extern {
    def sha256(hash: Ptr[UByte], payload: Ptr[UByte], len: CSize): Unit = extern
  }
}
