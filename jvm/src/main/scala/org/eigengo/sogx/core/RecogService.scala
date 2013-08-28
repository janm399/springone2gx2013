package org.eigengo.sogx.core

import org.springframework.messaging.core.MessageSendingOperations
import org.eigengo.sogx._
import scala.util.Random
import org.springframework.integration.MessageChannel
import org.springframework.integration.support.MessageBuilder

class RecogService(recogChannel: MessageChannel, messagingTemplate: MessageSendingOperations[String]) {

  private def sendWithContentType(contentType: String, correlationId: CorrelationId, chunk: ChunkData): Unit = {
    val message = MessageBuilder.
      withPayload(chunk).
      setCorrelationId(correlationId).
      setHeader("content-type", contentType).
      build()

    recogChannel.send(message)
  }

  def imageChunk(correlationId: CorrelationId, chunk: ChunkData) = sendWithContentType(ContentTypes.`image/*`, correlationId, chunk)

  def mjpegChunk(correlationId: CorrelationId, chunk: ChunkData) = sendWithContentType(ContentTypes.`video/mjpeg`, correlationId, chunk)

}
