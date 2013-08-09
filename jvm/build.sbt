import Dependencies._

name := "sogx"

organization := "org.eigengo"

version := "1.0"

scalaVersion := "2.10.2"

libraryDependencies ++= springframework.all ++ springintegration.all ++ jackson.all ++ reactor.all

libraryDependencies ++= Seq(
  servletApi % "provided",
  specs2     % "test"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

parallelExecution in Test := false

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
