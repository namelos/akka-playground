package aia.testdriven

import akka.actor.{Actor, ActorRef}

object SilentActor {
  case class SilentMessage(data: String)
  case class GetState(receiver: ActorRef)
}

class SilentActor extends Actor {
  import SilentActor._
  var internalState: Vector[String] = Vector()

  override def receive: Receive = {
    case SilentMessage(data) =>
      internalState = internalState :+ data
  }

  def state: Vector[String] = internalState
}
