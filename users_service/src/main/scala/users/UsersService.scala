package users

import akka.actor.{ActorRef, ActorSystem}

object UsersService extends App {

  val system: ActorSystem = ActorSystem("protobuf")

  val accountsActor = system.actorOf(Users.props("Howdy"), "accounts")

}