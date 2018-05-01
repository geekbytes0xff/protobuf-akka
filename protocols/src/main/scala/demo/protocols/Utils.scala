package demo.protocols

import scalapb.{GeneratedMessage, GeneratedMessageCompanion, Message}

trait Utils {
  def getManifest[P <: Message[P]](companion: GeneratedMessageCompanion[P]): Long = {
    companion.scalaDescriptor.getOptions.extension(PboptionsProto.manifest).get
  }

  def getManifest(message: GeneratedMessage): Long = {
    message.companion.scalaDescriptor.getOptions.extension(PboptionsProto.manifest).get
  }
}

object Utils extends Utils
