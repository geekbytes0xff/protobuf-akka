package demo.aggregator

import akka.actor.{ActorRef, ActorSystem}
import com.typesafe.config.ConfigFactory

object AccountsService extends App {

  //lazy val config = ConfigFactory.parseFile(new java.io.File("application.conf"))

  //println(config)

  val system: ActorSystem = ActorSystem("protobuf")


  val accountsActor = system.actorOf(Accounts.props(), "demo.accounts")


  accountsActor ! "sdfsdfsdf"

}