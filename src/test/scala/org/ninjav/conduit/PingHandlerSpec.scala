package org.ninjav.conduit

import akka.actor.ActorSystem
import akka.io.Tcp.{Write, Received}
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.ByteString
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

/**
 * Created by ninjav on 11/8/16.
 */
class PingHandlerSpec (system: ActorSystem)
  extends TestKit(system)
  with ImplicitSender
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("EchoHandlerSpec"))

  override def afterAll = {
    TestKit.shutdownActorSystem(system)
  }

  "A PingHandler" must {
    "echo the message reversed" in {
      val handler = system.actorOf(PingHandlerProps.props(testActor))
      val data = ByteString("hello")
      handler ! Received(data)
      expectMsg(Write(ByteString("olleh\n")))
    }
  }
}
