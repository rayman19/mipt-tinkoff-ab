ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "mipt-tinkoff-ab"
  )

libraryDependencies += "io.monix" %% "monix" % "3.3.0"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test

enablePlugins(ScalafmtPlugin)