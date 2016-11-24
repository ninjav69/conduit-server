package org.ninjav.conduit

import akka.actor.ActorSystem
import akka.io.Tcp.{Write, Received}
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.ByteString
import org.scalatest.{WordSpecLike, BeforeAndAfterAll, MustMatchers}

/**
 * Created by ninjav on 11/24/16.
 */
class RouterHandlerSpec(system: ActorSystem)
  extends TestKit(system)
  with ImplicitSender
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("RouterHandlerSpec"))

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A RouterHandler" must {

    "classify server message" in {
      val handler = system.actorOf(RouterHandlerProps.props(testActor))
      val data = ByteString("(ping:1234)")
      handler ! Received(data)
      expectMsg(Write(ByteString("Server message: ping:1234\n")))
    }

    "classify channel message" in {
      val handler = system.actorOf(RouterHandlerProps.props(testActor))
      val data = ByteString("[Client joined]")
      handler ! Received(data)
      expectMsg(Write(ByteString("Channel message: Client joined\n")))
    }

    "classify routed message without optional tag" in {
      val handler = system.actorOf(RouterHandlerProps.props(testActor))
      val data = ByteString("{0821230294C0823124232 C 3948293849}808080")
      handler ! Received(data)
      expectMsg(Write(ByteString("0821230294 doing C to 0823124232 [C 3948293849] 808080\n")))
    }

    "classify routed message with optional tag" in {
      val handler = system.actorOf(RouterHandlerProps.props(testActor))
      val data = ByteString("{0821230294C0823124232 C 3948293849}808080{.}")
      handler ! Received(data)
      expectMsg(Write(ByteString("0821230294 doing C to 0823124232 [C 3948293849] 808080 .\n")))
    }

    "classify simple message" in {
      val handler = system.actorOf(RouterHandlerProps.props(testActor))
      val data = ByteString("hello doos")
      handler ! Received(data)
      expectMsg(Write(ByteString("Simple message: hello doos\n")))
    }
  }
}
