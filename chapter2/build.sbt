import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "nl.heikovdheijden"
ThisBuild / organizationName := "chapter2"


val kafkaVersion = "2.4.1"

resolvers ++= Seq(
 "apache" at "https://repository.apache.org/snapshots"
)


lazy val dependencies = Seq(
  scalaTest % Test,
  "com.fasterxml.jackson.core" % "jackson-core" % "2.10.3",
  "org.apache.logging.log4j" % "log4j-api" % "2.13.1",
  "org.apache.logging.log4j" % "log4j-core" % "2.13.1" % Runtime,
  "org.apache.kafka" % "kafka-streams" % kafkaVersion,
  "org.apache.kafka" % "kafka-clients" % kafkaVersion,
  "org.apache.kafka" %% "kafka-streams-scala" % "2.4.1"
)


lazy val root = (project in file("."))
  .settings(
    name := "chapter2",
    libraryDependencies ++= dependencies
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
