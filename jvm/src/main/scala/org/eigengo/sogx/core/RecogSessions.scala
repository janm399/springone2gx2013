package org.eigengo.sogx.core

import org.eigengo.sogx.{CorrelationId, CoinResponse, RecogSessionId}
import org.springframework.messaging.simp.SimpMessageSendingOperations
import java.util
import org.springframework.messaging.support.MessageBuilder
import scala.collection.JavaConversions

class RecogSessions(messageSender: SimpMessageSendingOperations) {
  //val sessions = new mutable.HashMap[SessionId, CoinResponse]()
  val sessions = new util.HashMap[RecogSessionId, CoinResponse]()

  def sessionStarted(sessionId: RecogSessionId): Unit = {

  }

  def onCoinResponse(correlationId: CorrelationId, coins: CoinResponse): Unit = {
    //sessions += (SessionId(correlationId.value) -> coins)
    sessions.put(RecogSessionId(correlationId.value), coins)
    sendSessions()
  }

  def sessionEnded(sessionId: RecogSessionId): Unit = {
    //sessions -= sessionId
    sessions.remove(sessionId)
    sendSessions()
  }

  private def sendSessions(): Unit = {
    import JavaConversions._
    val b = new StringBuilder
    b.append("[")
    sessions.values().foreach(s => if (b.length == 1) b.append(s) else {b.append(","); b.append(s)})
    b.append("]")
    messageSender.send(s"/topic/recog/sessions", MessageBuilder.withPayload(b.toString().getBytes).build())
    // messageSender.convertAndSend(s"/topic/recog/sessions", sessions.values())
  }

}
