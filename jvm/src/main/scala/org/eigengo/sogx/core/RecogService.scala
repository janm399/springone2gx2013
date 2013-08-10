package org.eigengo.sogx.core

import org.springframework.messaging.core.MessageSendingOperations
import org.springframework.scheduling.annotation.Scheduled
import org.eigengo.sogx.{CoinResponse, Coin}
import java.util.UUID
import scala.util.Random

class RecogService (messagingTemplate: MessageSendingOperations[String]) {
  val random = new Random()

  @Scheduled(fixedDelay = 1000)
  def sendQuotes(): Unit = {
    val width = 800
    val height = 600
    val count = random.nextInt(10)

    val coinResponse = CoinResponse((0 to count).map(_ => Coin((random.nextInt(width), random.nextInt(height)), random.nextInt(100))).toArray, true)
    messagingTemplate.convertAndSend(s"/topic/recog/coin.${UUID.randomUUID()}", coinResponse)
  }

}
