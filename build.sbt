name := "protobuf-akka-demo"

version := "0.1"

scalaVersion := "2.12.6"

//val cleanProtocols = taskKey[Unit]("Cleans the protobuf generated code")


/*
cleanProtocols in Compile := {
  ((sourceDirectory in Compile).value / "scala/protocols/").listFiles.foreach(IO.delete)
}


PB.generate in Compile := {
  (cleanProtocols in Compile).value
  (PB.generate in Compile).value
}*/

//resolvers += "krasserm at bintray" at "http://dl.bintray.com/krasserm/maven"


updateOptions := updateOptions.value.withCachedResolution(false)

lazy val protocols = {
  (project in file("protocols"))
    .settings(
      PB.targets in Compile := Seq(
        scalapb.gen(
          flatPackage = false,
          javaConversions = false,
          grpc = false,
          singleLineToProtoString = true
        ) -> (sourceDirectory in Compile).value / "scala"
      )
    )
    .settings(
      updateOptions := updateOptions.value.withCachedResolution(false),
      scalaVersion := "2.12.6"
    )
    .settings(libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf")
    .settings(libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    ))

}

lazy val akkaVersion = "2.5.6"

lazy val seed = {
  (project in file("seed"))
    .settings(libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.84"
    ))
    .settings(
      updateOptions := updateOptions.value.withCachedResolution(false),
      scalaVersion := "2.12.6"
    )
}

lazy val generator = {
  (project in file("generator"))
    .settings(libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.84",
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    ))
    .settings(
      updateOptions := updateOptions.value.withCachedResolution(false),
      scalaVersion := "2.12.6"
    )
    .dependsOn(protocols)
}


lazy val aggregator = {
  (project in file("aggregator"))
    .settings(libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.84",
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    ))
    .settings(
      updateOptions := updateOptions.value.withCachedResolution(false),
      scalaVersion := "2.12.6"
    )
    .dependsOn(protocols)
}
