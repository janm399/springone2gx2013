package org.eigengo.sogx.core

import org.springframework.integration.annotation.{Header, Payload}
import java.util.UUID
import org.eigengo.sogx.ContentTypes._

class ChunkDecoder {

  def decodeFrame(@Header correlationId: UUID, @Header("content-type") contentType: String,
                  @Payload chunk: Array[Byte]): Array[Byte] = contentType match {
    case `video/h264` => decodeH264Frame(correlationId, chunk)
    case `image/*`    => decodeSingleImage(correlationId, chunk)
  }

  private def decodeSingleImage(correlationId: UUID, chunk: Array[Byte]): Array[Byte] = chunk

  private def decodeH264Frame(correlationId: UUID, chunk: Array[Byte]): Array[Byte] = {
    println("Decoding...")
    if (System.currentTimeMillis() % 5 == 0) {
      println("Got frame")
      chunk
    } else {
      println("No frame")
      null
    }
  }

}
