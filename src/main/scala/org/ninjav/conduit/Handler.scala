package org.ninjav.conduit

import akka.actor.{Actor, Props, ActorRef}
import akka.io.Tcp._

/**
 * Created by ninjav on 11/8/16.
 */

trait HandlerProps {
  def props(connection: ActorRef): Props
}

abstract class Handler(val connection: ActorRef) extends Actor {
  val abort = "(?i)abort".r
  val confirmedClose = "(?i)confirmedclose".r
  val close = "(?i)close".r

  def receive: Receive = {
    case Received(data) =>
      data.utf8String.trim match {
        case abort() => connection ! Abort
        case confirmedClose() => connection ! ConfirmedClose
        case close() => connection ! Close
        case str => received(str)
      }
    case PeerClosed =>
      peerClosed()
      stop()
    case ErrorClosed(cause: String) =>
      stop()
    case Closed =>
      closed()
      stop()
    case ConfirmedClosed =>
      confirmedClosed()
      stop()
    case Aborted =>
      aborted()
      stop()
  }

  def received(str: String): Unit

  def peerClosed(): Unit = {
    println("Peer Closed")
  }

  def errorClosed(): Unit = {
    println("Error Closed")
  }

  def closed(): Unit = {
    println("Closed")
  }

  def confirmedClosed(): Unit = {
    println("ConfirmedClosed")
  }

  def aborted(): Unit = {
    println("Aborted")
  }

  def stop(): Unit = {
    println("Stopping")
    context stop self
  }
}
