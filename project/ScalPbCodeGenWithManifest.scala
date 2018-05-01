import protocbridge.ProtocCodeGenerator
import sbt.{File, Logger}
import java.lang
import java.util.Date

import com.google.protobuf.DescriptorProtos.{DescriptorProto, FileDescriptorProto}
import com.google.protobuf.{DescriptorProtos, ExtensionRegistry}
import com.google.protobuf.compiler.PluginProtos.{CodeGeneratorRequest, CodeGeneratorResponse}
import scalapb.options.compiler.Scalapb.MessageOptions
import scalapb.compiler.ProtobufGenerator
import scalapb.options.compiler.Scalapb
import protocbridge.{Artifact, JvmGenerator, ProtocCodeGenerator}
import sbt.{File, IO, Logger}
import scalapb.ScalaPbCodeGenerator

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}
import Predef._

class ScalPbCodeGenWithManifest(log: Logger, protocolsRegistryFile: File) extends ProtocCodeGenerator {
  override def run(req: Array[Byte]): Array[Byte] = {

    log.info("Initializing scalaPb code generator")

    val registry = ExtensionRegistry.newInstance()
    Scalapb.registerAllExtensions(registry)
    val request = CodeGeneratorRequest.parseFrom(req, registry)


    val messages: List[(DescriptorProto, FileDescriptorProto)] = request.getProtoFileList.asScala.toList.flatMap(file => {
      if (file.hasPackage && file.getPackage.startsWith("demo.protocols")) {
        file.getMessageTypeList.asScala.toList.map((_, file))
      } else {
        Nil
      }
    })

    log.info(s"${messages.length} message definitions found")

    log.debug("validating manifests for messages ...")

    def checkMessages(manifests: List[(Long, DescriptorProto, FileDescriptorProto)], messages: List[(DescriptorProto, FileDescriptorProto)]): Either[String, List[(Long, DescriptorProto, FileDescriptorProto)]] = {
      messages match {
        case Nil => Right(manifests)
        case (message, file) :: rest => message.getOptions.getUnknownFields.getField(59367).getVarintList.asScala.headOption match {
          case Some(manifest) if manifests.exists(_._1 == manifest) => {
            Left(s"duplicate manifest found for message [${message.getName}]")
          }
          case Some(manifest) => {
            checkMessages((manifest.asInstanceOf[Long], message, file) :: manifests, rest)
          }
          case None => Left(s"no manifest found for message [${message.getName}]")
        }
      }
    }

    def generateSchemaRegistry(manifests: List[(Long, DescriptorProto, FileDescriptorProto)]): Unit = {

      log.debug("generating registry ...")

      val registrySource =
        s"""
           |package demo
           |
           |import scalapb.GeneratedMessage
           |import scala.util.Try
           |
           |/**
           |  * Sbt generated file.
           |  * ======================================
           |  *           DON'T ALTER
           |  * ======================================
           |  * Local time : ${new Date().toString}
           |  */
           |
           |object Registry {
           |
           |  lazy val parsers: Map[Long, Array[Byte] => Try[GeneratedMessage]] = Map[Long, Array[Byte] => Try[GeneratedMessage]](
           |    ${manifests.map(msg => s"${msg._1}L -> {(bytes:Array[Byte]) => ${msg._3.getPackage}.${msg._2.getName}.validate(bytes)}").mkString(",\n    ")}
           |  )
           |
           |}
           |
       """.stripMargin

      IO.write(protocolsRegistryFile, registrySource)

      log.info("finished writing schema files ...")
    }


    checkMessages(Nil, messages) match {
      case Left(error) => {
        log.error(error)
        CodeGeneratorResponse.newBuilder().setError("proto validation failed").build().toByteArray
      }
      case Right(messages) => {
        log.info("validating manifests for messages success")
        generateSchemaRegistry(messages)
        ProtobufGenerator.handleCodeGeneratorRequest(request).toByteArray
      }
    }


  }


  override def suggestedDependencies: Seq[Artifact] = Seq(
    Artifact("com.thesamet.scalapb", "scalapb-runtime", scalapb.compiler.Version.scalapbVersion, crossVersion = true)
  )
}

object ScalPbCodeGenWithManifest {
  def apply(log: Logger, protocolsRegistryFile: File): (JvmGenerator, Seq[String]) = {
    (JvmGenerator(
      "scala",
      new ScalPbCodeGenWithManifest(log, protocolsRegistryFile)),
      Seq(
        "flat_package" -> false,
        "java_conversions" -> false,
        "grpc" -> false,
        "single_line_to_proto_string" -> true,
        "ascii_format_to_string" -> true
      ).collect { case (name, v) if v => name })
  }
}
