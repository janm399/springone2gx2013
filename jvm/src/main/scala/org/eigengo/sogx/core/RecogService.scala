package org.eigengo.sogx.core

import org.springframework.messaging.core.MessageSendingOperations
import org.eigengo.sogx._
import scala.util.Random
import org.springframework.integration.MessageChannel
import org.springframework.integration.support.MessageBuilder

class RecogService(recogChannel: MessageChannel, messagingTemplate: MessageSendingOperations[String]) {
  val random = new Random()

  private def sendWithContentType(contentType: String)(correlationId: CorrelationId, chunk: ChunkData): Unit = {
    val message = MessageBuilder.
      withPayload(chunk).
      setCorrelationId(correlationId).
      setHeader("content-type", contentType).
      build()

    recogChannel.send(message)
  }

  def image(correlationId: CorrelationId, chunk: ChunkData) = sendWithContentType(ContentTypes.`image/*`) _

  def h264Frame(correlationId: CorrelationId, frame: ChunkData) = sendWithContentType(ContentTypes.`video/h264`) _

  def mjpegFrame(correlationId: CorrelationId, frame: ChunkData) = sendWithContentType(ContentTypes.`video/mjpeg`) _

}
