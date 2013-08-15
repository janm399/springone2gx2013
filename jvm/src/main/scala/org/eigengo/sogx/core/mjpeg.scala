package org.eigengo.sogx.core

import java.util
import org.eigengo.sogx._
import scala.collection.mutable.ArrayBuffer
import java.util.Collections

class MJPEGDecoder {

  def decodeFrames(correlationId: CorrelationId, chunk: ChunkData): util.Collection[ImageData] = {
    Collections.singletonList(chunk)
  }

}

case class ChunkingDecoderContext[U](f: Array[Byte] => U) {
  val buffer: ArrayBuffer[Byte] = ArrayBuffer()

  def decode(chunk: Array[Byte], end: Boolean) {
    if (buffer.isEmpty && end) {
      f(chunk)
    } else {
      buffer ++= chunk
      if (end) {
        f(buffer.toArray)
        buffer clear()
      }
    }
  }

  def close(): Unit = {}
}
