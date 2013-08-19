package org.eigengo.sogx.core

import java.util
import org.eigengo.sogx._
import scala.collection.mutable.ArrayBuffer
import java.util.{Collections, UUID}
import scala.collection.mutable

class MJPEGDecoder {
  val sessions = mutable.HashMap[CorrelationId, ChunkingDecoderContext]()

  def decodeFrames(correlationId: CorrelationId, chunk: Chunk): util.Collection[ImageData] = {
    //val buffer = new util.ArrayList[ChunkData]()
    //sessions.getOrElseUpdate(correlationId, new ChunkingDecoderContext(correlationId)).decode(chunk)(buffer.add)
    //buffer
    Collections.singletonList(chunk.data)
  }

}

private[core] case class ChunkingDecoderContext(correlationId: CorrelationId) {
  val buffer: ArrayBuffer[Byte] = ArrayBuffer()

  def decode[U](chunk: Chunk)(f: ImageData => U) {
    if (buffer.isEmpty && chunk.end) {
      f(chunk.data)
    } else {
      buffer ++= chunk.data
      if (chunk.end) {
        f(buffer.toArray)
        buffer clear()
      }
    }
  }

  def close(): Unit = {}
}
