
package demo

import scalapb.GeneratedMessage
import scala.util.Try

/**
  * Sbt generated file.
  * ======================================
  *           DON'T ALTER
  * ======================================
  * Local time : Tue May 01 19:33:17 BST 2018
  */

object Registry {

  lazy val parsers: Map[Long, Array[Byte] => Try[GeneratedMessage]] = Map[Long, Array[Byte] => Try[GeneratedMessage]](
    3L -> {(bytes:Array[Byte]) => demo.protocols.aggregator.state.Aggregate.validate(bytes)},
    2L -> {(bytes:Array[Byte]) => demo.protocols.aggregator.events.NumberAdded.validate(bytes)},
    1L -> {(bytes:Array[Byte]) => demo.protocols.aggregator.commands.AddNumber.validate(bytes)}
  )

}

       