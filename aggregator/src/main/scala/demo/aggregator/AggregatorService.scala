package demo.aggregator

import akka.actor.{ActorRef, ActorSystem}

object AggregatorService extends App {

  val system: ActorSystem = ActorSystem("protobuf")

  val accountsActor = system.actorOf(Aggregator.props(), "aggregator")

}