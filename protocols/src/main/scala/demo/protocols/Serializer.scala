package demo.protocols

import com.typesafe.scalalogging.LazyLogging
import demo.Registry
import scalapb.GeneratedMessage

import scala.util.{Failure, Success, Try}

class Serializer extends akka.serialization.SerializerWithStringManifest with Utils with LazyLogging {
  override def identifier: Int = 61551

  override def manifest(o: AnyRef): String = {
    logger.debug("serializing proto message {}", o)
    o match {
      case message: GeneratedMessage => getManifest(message).toString
      case _ => {
        logger.error("unknown object in proto serializer")
        throw new Exception("unknown object in proto serializer")
      }
    }
  }

  override def toBinary(o: AnyRef): Array[Byte] = {
    o match {
      case message: GeneratedMessage => message.toByteArray
      case _ => {
        logger.error("unknown object in proto serializer")
        throw new Exception("unknown object in proto serializer")
      }
    }
  }

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = {
    val o = Try {
      manifest.toLong
    }.flatMap(manifest => {
      Registry.parsers.get(manifest) match {
        case Some(parser) => parser(bytes)
        case None => {
          logger.error("undetected message type on deserialize {}", manifest)
          Failure(new Exception("undetected message type on deserialize"))
        }
      }
    }).get
    logger.debug("deserialized proto message {}", o)
    o
  }
}
