scalaVersion := "3.1.1"

enablePlugins(ScalaNativePlugin)

libraryDependencies += "com.lihaoyi" %%% "utest" % "0.7.11" % Test
testFrameworks += new TestFramework("utest.runner.Framework")
nativeLinkStubs := true
