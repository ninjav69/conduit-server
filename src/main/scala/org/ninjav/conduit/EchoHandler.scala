package org.ninjav.conduit

import akka.actor.{Props, ActorRef}
import akka.io.Tcp.Write
import akka.util.ByteString

/**
 * Created by ninjav on 11/8/16.
 */

object EchoHandlerProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[EchoHandler], connection)
}

class EchoHandler(connection: ActorRef) extends Handler(connection) {
  def received(data: String) = connection ! Write(ByteString(data + "\n"))
}
