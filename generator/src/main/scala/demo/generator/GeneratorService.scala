package demo.generator

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import demo.protocols.Utils
import demo.protocols.aggregator.commands.AddNumber

import scala.util.Try
import io.circe._, io.circe.generic.auto._, io.circe.syntax._

object GeneratorService extends App with LazyLogging  with Utils{

  //lazy val config = ConfigFactory.parseFile(new java.io.File("application.conf"))

  //println(config)

  val system: ActorSystem = ActorSystem("protobuf")


  val aggregator = system.actorSelection("akka.tcp://protobuf@127.0.0.1:2552/user/aggregator")

  println("Enter few Numbers >>")
  do {

    val int = Try{
      scala.io.StdIn.readInt()
    }

    int.foreach(i => {

      val cmd = AddNumber(i)
      logger.info("\n----sending command from generator---------\n{}\n----------------------" , prettyPrint(cmd))

      aggregator ! cmd
    })



  } while (true)


}
