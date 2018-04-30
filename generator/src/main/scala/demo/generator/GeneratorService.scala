package demo.generator

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import demo.protocols.aggregator.commands.AddNumber

object GeneratorService extends App with LazyLogging {

  //lazy val config = ConfigFactory.parseFile(new java.io.File("application.conf"))

  //println(config)

  val system: ActorSystem = ActorSystem("protobuf")


  val aggregator = system.actorSelection("akka.tcp://protobuf@127.0.0.1:2552/user/aggregator")

  println("Enter few Numbers >>")
  do {

    val int = scala.io.StdIn.readInt()

    aggregator ! AddNumber(int)

  } while (true)


}
