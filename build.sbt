
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

scalacOptions ++= Seq("-language:implicitConversions", "-deprecation")
lazy val root = (project in file("."))
  .settings(
    name := "Scala_rule_engine"
  )

libraryDependencies += "com.oracle.database.jdbc" % "ojdbc8" % "19.3.0.0"

