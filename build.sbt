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

lazy val akkaVersion = "2.5.12"

lazy val accounts_service  = {
  (project in file("accounts_service"))
    .settings(libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaVersion)

}
lazy val users_service  = {
  (project in file("users_service"))
    .settings(libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaVersion)

}
