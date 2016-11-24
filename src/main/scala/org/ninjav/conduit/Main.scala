package org.ninjav.conduit

import akka.actor.ActorSystem

/**
 * Created by ninjav on 11/8/16.
 */

object Main extends App {
  val system = ActorSystem("server")
  val service = system.actorOf(TcpServer.props(RouterHandlerProps), "ServerActor")

  //system.awaitTermination()
}
