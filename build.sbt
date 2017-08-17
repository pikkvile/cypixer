name := "cypixer"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.9.9",
  "org.json4s" %% "json4s-native" % "3.5.3",
  "com.netaporter" %% "scala-uri" % "0.4.16",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)