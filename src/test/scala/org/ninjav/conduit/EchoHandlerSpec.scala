package org.ninjav.conduit

import akka.actor.ActorSystem
import akka.io.Tcp.{Write, Received}
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.ByteString
import org.scalatest.{WordSpecLike, BeforeAndAfterAll, MustMatchers}

/**
 * Created by ninjav on 11/8/16.
 */
class EchoHandlerSpec(system: ActorSystem)
  extends TestKit(system)
  with ImplicitSender
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("EchoHandlerSpec"))

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A EchoHandler" must {

    "echo the message" in {
      val handler = system.actorOf(EchoHandlerProps.props(testActor))
      val data = ByteString("hello")
      handler ! Received(data)
      expectMsg(Write(ByteString("hello\n")))
    }
  }
}
