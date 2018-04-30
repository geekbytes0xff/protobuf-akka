package demo.aggregator

import akka.actor.{Actor, ActorRef, Props}
import akka.persistence.PersistentActor

object Accounts {
  def props(): Props = Props(new Accounts())
}

//#greeter-actor
class Accounts() extends PersistentActor {


  override def receiveCommand = {
    case a => println(a)
  }

  override def receiveRecover: Receive = {
    case _ => println()
  }

  override def persistenceId: String = "Accounts"
}