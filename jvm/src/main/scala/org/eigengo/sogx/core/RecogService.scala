package org.eigengo.sogx.core

import org.eigengo.sogx._
import org.springframework.integration.MessageChannel
import org.springframework.integration.support.MessageBuilder

/**
 * Gateway to the recognition flow. It receives a chunk of data with the given correlation id, constructs a message
 * with the appropriate headers and sends it down the ``recogChannel`` for processing.
 *
 * @param recogChannel the channel that the messages will be sent down
 */
class RecogService(recogChannel: MessageChannel) {

  private def sendWithContentType(contentType: String, correlationId: CorrelationId, chunk: ChunkData): Unit = {
    val message = MessageBuilder.
      withPayload(chunk).
      setCorrelationId(correlationId).
      setHeader("content-type", contentType).
      build()

    recogChannel.send(message)
  }

  /**
   * Submits a chunk of data that represents full image (JPEG, PNG, ...)
   *
   * @param correlationId the correlation id
   * @param chunk the chunk representing the entire image payload
   */
  def imageChunk(correlationId: CorrelationId)(chunk: ChunkData) = sendWithContentType(ContentTypes.`image/*`, correlationId, chunk)

  /**
   * Submits a chunk of data that represents portion of MJPEG video stream
   *
   * @param correlationId the correlation id
   * @param chunk the chunk of data of the MJPEG stream
   */
  def mjpegChunk(correlationId: CorrelationId)(chunk: ChunkData) = sendWithContentType(ContentTypes.`video/mjpeg`, correlationId, chunk)

}
