import scala.scalanative.unsigned._
import utest._
import sha256.Sha256.sha256

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
        "A linha não respondia; ia andando. Buraco aberto pela agulha era logo enchido por ela, silenciosa e ativa, como quem sabe o que faz, e não está para ouvir palavras loucas. A agulha, vendo que ela não lhe dava resposta, calou-se também, e foi andando. E era tudo silêncio na saleta de costura; não se ouvia mais que o plic-plic-plic-plic da agulha no pano. Caindo o sol, a costureira dobrou a costura, para o dia seguinte. Continuou ainda nessa e no outro, até que no quarto acabou a obra, e ficou esperando o baile."
      )

      r.size ==> 32
      bytearrayToHex(r) ==>
        "1f63ba66aeb77982ce4e730c1849cc49593ee366bf0145167ecb2390be593b20"
    }

    test("bytearray hashing") {
      val r = sha256(
        Array[UByte](2.toUByte, 4.toUByte, 201.toUByte, 203.toUByte)
      )

      r.size ==> 32
      bytearrayToHex(r) ==>
        "f67fcc16df631caade8ced02f61349cdca9e1456eb6e4033f6001520862edc4e"
    }

    test("big bytearray hashing") {
      val r = sha256(Array.tabulate[UByte](256)(i => i.toUByte))

      r.size ==> 32
      bytearrayToHex(r) ==>
        "40aff2e9d2d8922e47afd4648e6967497158785fbd1da870e7110266bf944880"
    }
  }
}
