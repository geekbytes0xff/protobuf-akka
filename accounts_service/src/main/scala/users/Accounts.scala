package users

import akka.actor.{Actor, ActorRef, Props}

object Accounts {
  def props(): Props = Props(new Accounts())
}

//#greeter-actor
class Accounts() extends Actor {


  def receive = {
    case a => println(a)
  }
}