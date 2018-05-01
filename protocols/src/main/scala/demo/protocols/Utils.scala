package demo.protocols

import demo.PboptionsProto

import scalapb.{GeneratedMessage, GeneratedMessageCompanion, Message}

trait Utils {
  def getManifest[P <: Message[P] with GeneratedMessage](companion: GeneratedMessageCompanion[P]): Long = {
    companion.scalaDescriptor.getOptions.extension(PboptionsProto.manifest).get
  }

  def getManifest(message: GeneratedMessage): Long = {
    message.companion.scalaDescriptor.getOptions.extension(PboptionsProto.manifest).get
  }
}

object Utils extends Utils
