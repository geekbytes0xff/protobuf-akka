package demo

package object protocols {

  object events {

    case class NumberAdded(number: Int)

  }

  object commands {

    case class AddNumber(number: Int)

  }

  object state {

    case class Aggregate(value: Int)

  }


}
