import scala.scalanative.unsigned._
import utest._
import Sha256.sha256

object Sha256Test extends TestSuite {
  def bytearrayToHex(ba: Array[UByte]): String =
    ba.map(_.toHexString)
      .map(x =>
        if (x.size == 2) x
        else s"0$x"
      )
      .mkString

  val tests = Tests {
    test("string hashing") {
      val r = sha256(
        "a"
      )

      r.size ==> 32
      bytearrayToHex(r) ==>
        "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb"
    }

    test("big string hashing") {
      val r = sha256(
        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      )

      r.size ==> 32
      bytearrayToHex(r) ==>
        "f54353008a2553262ecdc4a34749563ba0950e8b0fc8652780b0a614b99683c1"
    }

    test("bytearray hashing") {
      val r = sha256(
        Array[Byte](1, 2, 3, 4)
      )

      r.size ==> 32
      bytearrayToHex(r) ==>
        "9f64a747e1b97f131fabb6b447296c9b6f0201e79fb3c5356e6c77e89b6a806a"
    }

    test("big bytearray hashing") {
      val r = sha256(Array.tabulate[Byte](127)(i => i.toByte))

      r.size ==> 32
      bytearrayToHex(r) ==>
        "92ca0fa6651ee2f97b884b7246a562fa71250fedefe5ebf270d31c546bfea976"
    }
  }
}
