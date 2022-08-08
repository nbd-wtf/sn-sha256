import scala.scalanative.unsigned._
import utest._

object Sha256EtcTests extends TestSuite {
  def bytes2hex(ba: Array[UByte]): String =
    ba.map(_.toHexString)
      .map(x =>
        if (x.size == 2) x
        else s"0$x"
      )
      .mkString

  def hex2bytes(hex: String): Array[UByte] =
    hex
      .sliding(2, 2)
      .toArray[String]
      .map(Integer.parseInt(_, 16).toByte.toUByte)

  val tests = Tests {
    test("string hashing") {
      val r = sha256.sha256(
        "a"
      )

      r.size ==> 32
      bytes2hex(r) ==>
        "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb"
    }

    test("big string hashing") {
      val r = sha256.sha256(
        "A linha não respondia; ia andando. Buraco aberto pela agulha era logo enchido por ela, silenciosa e ativa, como quem sabe o que faz, e não está para ouvir palavras loucas. A agulha, vendo que ela não lhe dava resposta, calou-se também, e foi andando. E era tudo silêncio na saleta de costura; não se ouvia mais que o plic-plic-plic-plic da agulha no pano. Caindo o sol, a costureira dobrou a costura, para o dia seguinte. Continuou ainda nessa e no outro, até que no quarto acabou a obra, e ficou esperando o baile."
      )

      r.size ==> 32
      bytes2hex(r) ==>
        "1f63ba66aeb77982ce4e730c1849cc49593ee366bf0145167ecb2390be593b20"
    }

    test("bytearray hashing") {
      val r = sha256.sha256(
        Array[UByte](2.toUByte, 4.toUByte, 201.toUByte, 203.toUByte)
      )

      r.size ==> 32
      bytes2hex(r) ==>
        "f67fcc16df631caade8ced02f61349cdca9e1456eb6e4033f6001520862edc4e"
    }

    test("big bytearray hashing") {
      val r = sha256.sha256(Array.tabulate[UByte](256)(i => i.toUByte))

      r.size ==> 32
      bytes2hex(r) ==>
        "40aff2e9d2d8922e47afd4648e6967497158785fbd1da870e7110266bf944880"
    }

    test("hkdf") {
      val secret = hex2bytes(
        "40d0542e65d5bdb096051a21aa9af7eebfa3cf4f8e3cd367bc408e4fa91629b3"
      )
      val info = "nodeid".getBytes.map(_.toUByte)
      val salt = Array(0.toUByte)

      val r = hkdf256.hkdf(salt, secret, info, 32)
      r.size ==> 32
      bytes2hex(
        r
      ) ==> "a754786431d51674edc4f04a568a55f37b996db70fa2a795b15e2ea57cfed8be"
    }

    test("hmac") {
      val key = "abcdef".getBytes.map(_.toUByte)
      val msg = "123456".getBytes.map(_.toUByte)

      val r = hmac256.hmac(key, msg)
      r.size ==> 32
      bytes2hex(
        r
      ) ==> "ec4a11a5568e5cfdb5fbfe7152e8920d7bad864a0645c57fe49046a3e81ec91d"
    }

    test("sha512") {
      bytes2hex(
        sha512.sha512("banana")
      ) ==> "f8e3183d38e6c51889582cb260ab825252f395b4ac8fb0e6b13e9a71f7c10a80d5301e4a949f2783cb0c20205f1d850f87045f4420ad2271c8fd5f0cd8944be3"
    }

    test("ripemd160") {
      bytes2hex(
        ripemd160.ripemd160("banana")
      ) ==> "e8f92e55b15aec83f458cfee39dd4ffeb7f4b8ed"
    }

    test("hmac512") {
      val key = "abcdef".getBytes.map(_.toUByte)
      val msg = "123456".getBytes.map(_.toUByte)

      val r = hmac512.hmac(key, msg)
      r.size ==> 64
      bytes2hex(
        r
      ) ==> "130a4caafb11b798dd7528628d21f742feaad266e66141cc2ac003f0e6437cb5749245af8a3018d354e4b55e14703a5966808438afe4aae516d2824b014b5902"
    }
  }
}
