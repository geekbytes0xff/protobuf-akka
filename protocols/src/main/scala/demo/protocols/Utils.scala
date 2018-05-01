package demo.protocols

import io.circe._, io.circe.generic.auto._, io.circe.syntax._
import demo.PboptionsProto
import scalapb.{GeneratedMessage, GeneratedMessageCompanion, Message}

trait Utils {
  def getManifest[P <: Message[P] with GeneratedMessage](companion: GeneratedMessageCompanion[P]): Long = {
    companion.scalaDescriptor.getOptions.extension(PboptionsProto.manifest).get
  }

  def getManifest(message: GeneratedMessage): Long = {
    message.companion.scalaDescriptor.getOptions.extension(PboptionsProto.manifest).get
  }

  def prettyPrint[A](obj: => A)(implicit encoder: Encoder[A]): String = {
    s"${obj.getClass.getSimpleName}${obj.asJson.noSpaces}"
  }
}

object Utils extends Utils
