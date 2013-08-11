package org.eigengo.sogx.core

import org.springframework.integration.annotation.{Header, Payload}
import java.util.{Collections, UUID}
import org.eigengo.sogx.ContentTypes._
import java.util

class ChunkDecoder {

  def decodeFrame(@Header correlationId: UUID, @Header("content-type") contentType: String,
                  @Payload chunk: Array[Byte]): util.Collection[Array[Byte]] = contentType match {
    case `video/h264` => decodeH264Frames(correlationId, chunk)
    case `image/*`    => decodeSingleImage(correlationId, chunk)
  }

  private def decodeSingleImage(session: UUID, chunk: Array[Byte]): util.Collection[Array[Byte]] = Collections.singletonList(chunk)

  private def decodeH264Frames(session: UUID, chunk: Array[Byte]): util.Collection[Array[Byte]] = H264Decoder.decodeFrames(session, chunk)

}
