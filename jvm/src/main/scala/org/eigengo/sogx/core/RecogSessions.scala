package org.eigengo.sogx.core

import org.eigengo.sogx.{CorrelationId, CoinResponse, RecogSessionId}
import org.springframework.messaging.simp.SimpMessageSendingOperations
import java.util

class RecogSessions(messageSender: SimpMessageSendingOperations) {
  val sessions = new util.HashMap[RecogSessionId, CoinResponse]()

  def onCoinResponse(correlationId: CorrelationId, coins: CoinResponse): Unit = {
    sessions.put(RecogSessionId(correlationId.value), coins)
    sendSessions()
  }

  def sessionEnded(sessionId: RecogSessionId): Unit = {
    sessions.remove(sessionId)
    sendSessions()
  }

  private def sendSessions(): Unit = messageSender.convertAndSend(s"/topic/recog/sessions", sessions.values())

}
