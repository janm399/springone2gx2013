package org.eigengo.sogx.core

import org.springframework.stereotype.Service
import org.springframework.messaging.core.MessageSendingOperations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.eigengo.sogx.Coin
import java.util.UUID

// @Service
class RecogService (messagingTemplate: MessageSendingOperations[String]) {

  @Scheduled(fixedDelay = 1000)
  def sendQuotes(): Unit = {
    messagingTemplate.convertAndSend(
      s"/topic/recog/coin.${UUID.randomUUID()}",
      """{"foo":"bar"}""")
  }

}
