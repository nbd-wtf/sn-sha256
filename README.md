sn-sha256
=========

SHA256 + HMAC + HKDF implementations in C for use in Scala 3 Native.
Copied from https://github.com/rustyrussell/ccan.

Installation
------------

```sbt
libraryDependencies += "com.fiatjaf" %%% "sn-sha256" % "0.3.0"
```

Usage
-----

This library provides: `Sha256.sha256()`, `Hmac.hmac()` and `Hkdf.hkdf()`. Use it as follows:

```scala
import scala.scalanative.unsigned._
import sha256.{Sha256, Hkdf, Hmac}

// basic sha256
Sha256.sha256("a string") // ==> Array[UByte](192, 220, 134, 239, 218, 0, 96, 212, 8, 64, 152, 169, 14, 201, 43, 61, 74, 168, 157, 127, 126, 15, 186, 84, 36, 86, 29, 33, 69, 30, 23, 88)
val r = Sha256.sha256(Array[UByte](12.toUByte, 23.toUByte, 244.toUByte, 180.toUByte))
bytes2hex(r) // ==> "c8010c6ca931acf480fab946f52d6abf81de50f797a666017cb4f0347c0974f9"

// hmac-sha256
val r = Hmac.hmac(
  "123456".getBytes.map(_.toUByte)
  "abcdef".getBytes.map(_.toUByte),
)
bytes2hex(r) // ==> "ec4a11a5568e5cfdb5fbfe7152e8920d7bad864a0645c57fe49046a3e81ec91d"

// hkdf-sha256
val secret = hex2bytes("40d0542e65d5bdb096051a21aa9af7eebfa3cf4f8e3cd367bc408e4fa91629b3")
val info = "nodeid".getBytes.map(_.toUByte)
val salt = Array(0.toUByte)
val r = Hkdf.hkdf(salt, secret, info, 32)
bytes2hex(r) // ==> "a754786431d51674edc4f04a568a55f37b996db70fa2a795b15e2ea57cfed8be"
```
