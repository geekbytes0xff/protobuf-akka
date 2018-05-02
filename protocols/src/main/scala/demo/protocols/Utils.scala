package demo.protocols

import io.circe._, io.circe.generic.auto._, io.circe.syntax._
import demo.PboptionsProto
import scalapb.{GeneratedMessage, GeneratedMessageCompanion, Message}

trait Utils {

  def prettyPrint[A](obj: => A)(implicit encoder: Encoder[A]): String = {
    s"${obj.getClass.getSimpleName}${obj.asJson.noSpaces}"
  }
}

object Utils extends Utils
