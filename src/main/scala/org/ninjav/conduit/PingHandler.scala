package org.ninjav.conduit

import akka.actor.{Props, ActorRef}
import akka.io.Tcp.Write
import akka.util.ByteString

/**
 * Created by ninjav on 11/8/16.
 */

object PingHandlerProps extends HandlerProps {
  override def props(connection: ActorRef): Props = Props(classOf[PingHandler], connection)
}

class PingHandler(connection : ActorRef) extends Handler(connection) {
  override def received(data: String): Unit = connection ! Write(ByteString(data.reverse + '\n'))
}
