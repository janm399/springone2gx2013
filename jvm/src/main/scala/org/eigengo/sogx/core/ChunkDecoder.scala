package org.eigengo.sogx.core

import org.springframework.integration.annotation.{Header, Payload}
import java.util.Collections
import org.eigengo.sogx.ContentTypes._
import org.eigengo.sogx._
import java.util

/**
 * Decodes the chunks
 *
 * @param mjpegDecoder
 */
class ChunkDecoder(mjpegDecoder: MJPEGDecoder) {

  def decodeFrame(@Header correlationId: CorrelationId, @Header("content-type") contentType: String,
                  @Payload chunk: ChunkData): util.Collection[ImageData] = contentType match {
    case `video/mjpeg` => decodeMJPEGFrames(correlationId, chunk)
    case `image/*`     => decodeSingleImage(correlationId, chunk)
  }

  private def decodeSingleImage(correlationId: CorrelationId, chunk: ChunkData): util.Collection[ImageData] = Collections.singletonList(chunk)

  private def decodeMJPEGFrames(correlationId: CorrelationId, chunk: ChunkData): util.Collection[ImageData] = mjpegDecoder.decodeFrames(correlationId, chunk)

}
