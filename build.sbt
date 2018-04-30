name := "protobuf-akka-demo"

version := "0.1"

scalaVersion := "2.12.3"

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
    .settings(libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf")

}

lazy val akkaVersion = "2.5.6"

lazy val accounts_service  = {
  (project in file("accounts_service"))
    .settings(libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.84"
    ))
}


lazy val users_service  = {
  (project in file("users_service"))
    .settings(libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.84"
    ))
}
