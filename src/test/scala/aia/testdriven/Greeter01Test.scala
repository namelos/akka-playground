package aia.testdriven

import akka.actor
import akka.actor.{ActorSystem, Props}
import akka.testkit.{CallingThreadDispatcher, EventFilter, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.WordSpecLike

import Greeter01Test._

class Greeter01Test extends TestKit(testSystem)
  with WordSpecLike with StopSystemAfterAll {
  "The Greeter" must {
    "say Hello World! when a Greeting(\"world\") is sent it" in {
      val dispatcherId = CallingThreadDispatcher.Id
      val props = Props[Greeter].withDispatcher(dispatcherId)
      val greeter = system.actorOf(props)
      EventFilter.info(message = "Hello World!", occurrences = 1).intercept {
        greeter ! Greeting("World")
      }
    }
  }
}

object Greeter01Test {
  val testSystem: ActorSystem = {
    val config = ConfigFactory.parseString(
      """
        |akka.loggers = [akka.testkit.TestEventListener]
      """.stripMargin)

    ActorSystem("testsystem", config)
  }
}