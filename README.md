sn-sha256
=========

A SHA256 implementation in C for use in Scala 3 Native.

Installation
------------

```sbt
libraryDependencies += "com.fiatjaf" %%% "sn-sha256" % "0.2.0"
```

Usage
-----

This library provides a single `sha256()` function that takes either a `String` or an `Array[UByte]` and outputs an `Array[UByte]`.

```scala
import scala.scalanative.unsigned._
import sha256.Sha256.sha256

sha256("a string")
// ==> Array[UByte](192, 220, 134, 239, 218, 0, 96, 212, 8, 64, 152, 169, 14, 201, 43, 61, 74, 168, 157, 127, 126, 15, 186, 84, 36, 86, 29, 33, 69, 30, 23, 88)

sha256(Array[UByte](12.toUByte, 23.toUByte, 244.toUByte, 180.toUByte))
  .map(_.toHexString)
  .map(x => if (x.size == 2) x else s"0$x")
  .mkString
// ==> "c8010c6ca931acf480fab946f52d6abf81de50f797a666017cb4f0347c0974f9"
```
