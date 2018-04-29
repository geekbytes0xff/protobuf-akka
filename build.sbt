name := "protobuf-akka-demo"

version := "0.1"

scalaVersion := "2.12.6"

//val cleanProtocols = taskKey[Unit]("Cleans the protobuf generated code")

PB.targets in Compile := Seq(
  scalapb.gen(
    flatPackage = false,
    javaConversions = false,
    grpc = false,
    singleLineToProtoString = true
  ) -> (sourceDirectory in Compile).value / "scala"
)
/*
cleanProtocols in Compile := {
  ((sourceDirectory in Compile).value / "scala/protocols/").listFiles.foreach(IO.delete)
}


PB.generate in Compile := {
  (cleanProtocols in Compile).value
  (PB.generate in Compile).value
}*/

libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"