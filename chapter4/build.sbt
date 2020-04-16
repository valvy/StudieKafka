import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "nl.hvanderheijden"
ThisBuild / organizationName := "chapter4"

val kafkaVersion = "2.4.1"

lazy val dependencies = Seq(
  scalaTest % Test,
  "com.github.javafaker" % "javafaker" % "1.0.2",
  "commons-cli" % "commons-cli" % "1.3.1",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.3",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.10.3",
  "org.apache.kafka" % "kafka-streams" % kafkaVersion,
  "org.apache.kafka" % "kafka-clients" % kafkaVersion,
  "org.apache.kafka" %% "kafka-streams-scala" % "2.4.1"
)

/*assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}*/

lazy val root = (project in file("."))
  .settings(
    addCompilerPlugin(scalafixSemanticdb),
    name := "chapter4",
    libraryDependencies ++= dependencies
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
