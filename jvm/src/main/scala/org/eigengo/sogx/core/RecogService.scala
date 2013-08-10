package org.eigengo.sogx.core

import org.springframework.messaging.core.MessageSendingOperations
import org.springframework.scheduling.annotation.Scheduled
import org.eigengo.sogx.{CoinResponse, Coin}
import java.util.UUID

class RecogService (messagingTemplate: MessageSendingOperations[String]) {

  @Scheduled(fixedDelay = 1000)
  def sendQuotes(): Unit = {
    val count = (math.random * 10).toInt
    val coinResponse = CoinResponse((0 to count).map(_ => Coin(math.random, math.random)).toArray, true)
    messagingTemplate.convertAndSend(
      s"/topic/recog/coin.${UUID.randomUUID()}", coinResponse)
  }

}
