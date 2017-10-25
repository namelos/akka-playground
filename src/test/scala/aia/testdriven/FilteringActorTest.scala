package aia.testdriven

import akka.actor._
import akka.testkit.TestKit
import org.scalatest.{MustMatchers, WordSpecLike}

import scala.concurrent.duration._

class FilteringActorTest extends TestKit(ActorSystem())
  with WordSpecLike with MustMatchers with StopSystemAfterAll {
  "A Filtering Actor" must {
    "filter out particular messages" in {
      import FilteringActor._

      val props = FilteringActor.props(testActor, 5)
      val filter = system.actorOf(props, "filter-1")
      filter ! Event(1)
      filter ! Event(2)
      filter ! Event(1)
      filter ! Event(3)
      filter ! Event(1)
      filter ! Event(4)
      filter ! Event(5)
      filter ! Event(5)
      filter ! Event(6)

      val eventIds = receiveWhile() {
        case Event(id) if id <= 5 => id
      }

      eventIds mustBe List(1, 2, 3, 4, 5)
      expectMsg(Event(6))
    }

    "filter out particular message using expectNoMsg" in {
      import FilteringActor._

      val props = FilteringActor.props(testActor, 5)
      val filter = system.actorOf(props, "filter-2")
      filter ! Event(1)
      filter ! Event(2)
      expectMsg(Event(1))
      expectMsg(Event(2))
      filter ! Event(1)
      expectNoMessage(1 second)
      filter ! Event(3)
      expectMsg(Event(3))
      filter ! Event(1)
      expectNoMessage(1 second)
      filter ! Event(4)
      filter ! Event(5)
      filter ! Event(5)
      expectMsg(Event(4))
      expectMsg(Event(5))
      expectNoMessage(1 second)
    }
  }
}
