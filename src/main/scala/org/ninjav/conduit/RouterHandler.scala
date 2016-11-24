package org.ninjav.conduit

import akka.actor.{Props, ActorRef}
import akka.io.Tcp.Write
import akka.util.ByteString

/**
 * Created by ninjav on 11/24/16.
 *
 * The purpose of this handler is to catagorize a message depending on pattern
 * and dispatch to appropriate handlers.
 *
 * 4 patterns are supported designating Server, Channel, Routed and Simple messages,
 * each with it's own handler. Messages of each type are forwarded to their appropriate
 * handlers.
 */

object RouterHandlerProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[RouterHandler], connection)
}

class RouterHandler(connection: ActorRef) extends Handler(connection) {
  // Example: (ping:1234)
  val serverPattern = """^\((.*?)\)""".r

  // Example: [TestPanel joined]
  val channelPattern = """^\[(.*?)\]""".r

  // Example: {0821230294C0823124232 C 3948293849}808080{.}
  val routedPattern = """^\{(\d{10})(\D)(\d{10}) (.+)?\}([\w\d]+)(\{(.+?)\})?""".r

  // Example: hello doos
  val simplePattern = "^(.+)$".r

  override def received(str: String): Unit = {

    str match {
      case serverPattern(text) =>
        connection ! Write(ByteString("Server message: " + text + "\n"))

      case channelPattern(text) =>
        connection ! Write(ByteString("Channel message: " + text + "\n"))

      case routedPattern(source, command, dest, payload, crc, null, null) =>
        connection ! Write(ByteString(s"$source doing $command to $dest [$payload] $crc\n"))

      case routedPattern(source, command, dest, payload, crc, dummy, tag) =>
        connection ! Write(ByteString(s"$source doing $command to $dest [$payload] $crc $tag\n"))

      case simplePattern(text) =>
        connection ! Write(ByteString("Simple message: " + text + "\n"));

      case _ => println("Discarding blank message")
    }
  }
}
