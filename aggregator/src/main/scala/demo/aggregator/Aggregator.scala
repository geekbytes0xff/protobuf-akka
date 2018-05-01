package demo.aggregator

import akka.actor.Props
import akka.persistence.{PersistentActor, RecoveryCompleted}
import com.typesafe.scalalogging.LazyLogging
import demo.protocols.Utils
import demo.protocols.aggregator.commands.SumNumber
import demo.protocols.aggregator.events.NumberSummed
import demo.protocols.aggregator.state.Sum
import io.circe._, io.circe.generic.auto._, io.circe.syntax._

object Aggregator {
  def props(): Props = Props(new Aggregator())

}



class Aggregator() extends PersistentActor with LazyLogging with Utils {

  var state: Sum = Sum(0)

  def update(event: Any):Unit = event match {
    case e@NumberSummed(number) => {
      state = state.copy(state.total + number)
      logger.info("\n---- updating state ---------\nevent: {}\nnew state: {}\n --------------------", prettyPrint(e),prettyPrint(state))
    }
    case _=> ()
  }

  override def receiveCommand: Receive = {


    case x@SumNumber(number) => {
      logger.info("\n----Aggregator Received Command---------\n{}\n--------------------", prettyPrint(x))
      persist[NumberSummed](NumberSummed(number))(update)
    }
    case x => {
      logger.info("Aggregator Received UnKnown Command {}", x)
    }
  }

  override def receiveRecover: Receive = {
    case x: NumberSummed => update(x)
    case RecoveryCompleted => {
      logger.info("\n----Aggregator Recovery Complete with State---------\n{}\n-----------------", prettyPrint(state))
    }
  }


  override def persistenceId: String = "demo/users"
}