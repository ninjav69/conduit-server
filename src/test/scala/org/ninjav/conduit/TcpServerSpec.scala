package org.ninjav.conduit

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.io.Tcp.{Register, Connected}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{WordSpecLike, BeforeAndAfterAll, MustMatchers}

/**
 * Created by ninjav on 11/8/16.
 */
class TcpServerSpec(system: ActorSystem)
  extends TestKit(system)
  with ImplicitSender
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("TcpServerSpec"))

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A TcpServer actor" must {
    "register a handler when a client connected" in {
      val server = system.actorOf(TcpServer.props(EchoHandlerProps), "ServerActor")
      server ! Connected(new InetSocketAddress(5555),
        new InetSocketAddress(9000))
      expectMsgPF() { case _: Register => }
    }
  }
}
