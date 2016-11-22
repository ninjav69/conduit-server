package org.ninjav.conduit.rabbitmq

import akka.actor.Actor
import akka.stream.scaladsl.Source
import com.rabbitmq.client.AMQP.Queue
import io.scalac.amqp.Connection

import scala.concurrent.Future
import org.ninjav.conduit.rabbitmq.RabbitRegistry._

import scala.util.{Success, Failure}

/**
 * Created by ninjav on 11/10/16.
 */
class Consumer extends Actor {
  val connection = Connection()

  setupRabbit() onComplete {
    case Success(_) =>
      val rabbitConsumer = Source(connection.consume(inboundQueue.name))
      //val rabbitPublisher = Sink(connection.publish(outboundExchange.name))

      val flow = rabbitConsumer

      flow.run()
    case Failure(ex) =>
      logger.error("Failed to declare RabbitMQ infrastructure.", ex)
  }

  override def receive: Receive = {

  }

  def setupRabbit(): Future[List[Queue.BindOk]] =
    Future.sequence(List(

      /* declare and bind inbound exchange and queue */
      Future.sequence {
        connection.exchangeDeclare(inboundExchange) ::
        connection.queueDeclare(inboundQueue) :: Nil
      } flatMap { _ =>
        Future.sequence {
              connection.queueBind(inboundQueue.name, inboundExchange.name, "") :: Nil
        }
      },

      /* declare and bind outbound exchange and queues */
      Future.sequence {
        connection.exchangeDeclare(outboundExchange) ::
        connection.queueDeclare(outboundQueue) :: Nil
      } flatMap { _ =>
        Future.sequence {
          connection.queueBind(outboundQueue.name, outboundExchange.name, "") :: Nil
        }
      }
    )).map { _.flatten }

}
