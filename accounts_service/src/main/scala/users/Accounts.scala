package users

import akka.actor.{ActorRef, Props}

object Accounts {
  def props(usersActorRef: ActorRef): Props = Props(new Accounts(usersActorRef))
}

//#greeter-actor
class Accounts(usersActorRef: ActorRef) extends Actor {


  def receive = {

  }
}