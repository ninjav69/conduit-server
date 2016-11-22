package org.ninjav.conduit.rabbitmq

import io.scalac.amqp.{Queue, Direct, Exchange}

/**
 * Created by ninjav on 11/10/16.
 */
object RabbitRegistry {
  val inboundExchange = Exchange("conduit.inbound.exchange", Direct, true)
  val inboundQueue = Queue("conduit.inbound.queue")

  val outboundExchange = Exchange("conduit.outbound.exchange", Direct, true)
  val outboundQueue = Queue("conduit.outbound.queue")
}
