package org.eigengo.sogx.core

import org.springframework.messaging.core.MessageSendingOperations
import org.eigengo.sogx.ContentTypes
import java.util.UUID
import scala.util.Random
import org.springframework.integration.MessageChannel
import org.springframework.integration.support.MessageBuilder
import ContentTypes._

class RecogService(recogChannel: MessageChannel, messagingTemplate: MessageSendingOperations[String]) {
  val random = new Random()

  def image(session: UUID, chunk: Array[Byte]) = {
    val message = MessageBuilder.
      withPayload(chunk).
      setCorrelationId(session).
      setHeader("content-type", `image/*`).
      build()

    recogChannel.send(message)
  }

  def h264Frame(session: UUID, frame: Array[Byte]) = {
    val message = MessageBuilder.
      withPayload(frame).
      setCorrelationId(session).
      setHeader("content-type", `video/h264`).
      build()

    recogChannel.send(message)
  }

}
