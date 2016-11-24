name := """conduit-server"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.11" % "test",
  "com.typesafe.akka" %%  "akka-stream-experimental" % "0.10",
  "io.scalac" %%  "reactive-rabbit" % "0.2.1",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

libraryDependencies += "com.github.etaty" %% "rediscala" % "1.7.0"