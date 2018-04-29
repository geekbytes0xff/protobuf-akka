package users

import akka.actor.{Actor, ActorRef, Props}

object Users {
  def props(): Props = Props(new Users())
}

class Users() extends Actor {


  def receive = {

  }
}