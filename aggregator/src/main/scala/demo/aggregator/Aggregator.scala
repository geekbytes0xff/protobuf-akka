package demo.aggregator

import akka.actor.{Actor, ActorRef, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import com.typesafe.scalalogging.LazyLogging
import demo.protocols.aggregator.commands.AddNumber
import demo.protocols.aggregator.events.NumberAdded
import demo.protocols.aggregator.state.Aggregate

object Aggregator {
  def props(): Props = Props(new Aggregator())

}

class Aggregator() extends PersistentActor with LazyLogging {

  var state: Aggregate = Aggregate(0)

  def update(event: Any):Unit = event match {
    case NumberAdded(number) => state = state.copy(state.value + number)
  }

  override def receiveCommand: Receive = {
    case x@AddNumber(number) => {
      logger.info("Aggregator Received Command", x)
      persist[NumberAdded](NumberAdded(number))(update)
    }
    case x => {
      logger.info("Aggregator Received UnKnown Command", x)
    }
  }

  override def receiveRecover: Receive = {
    case x: NumberAdded => update(x)
    case RecoveryCompleted => {
      logger.info("Aggregator Recovery Complete State ", state)
    }
  }


  override def persistenceId: String = "demo/users"
}