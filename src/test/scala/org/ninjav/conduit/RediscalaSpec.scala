package org.ninjav.conduit

import akka.actor.ActorSystem
import akka.io.Tcp.{Received, Write}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}
import redis.RedisClient
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by ninjav on 11/8/16.
 */
class RediscalaSpec (system: ActorSystem)
  extends TestKit(system)
  with ImplicitSender
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("RediscalaSpec"))

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A RedisClient" must {

    "ping and pong redis server" in {
      val redis = RedisClient()(system)

      val futurePong = redis.ping()
      println("Ping sent!")
      futurePong.map(pong => {
        println(s"Redis replied with a $pong")
      })

      Await.result(futurePong, 5 seconds)
    }
  }
}
