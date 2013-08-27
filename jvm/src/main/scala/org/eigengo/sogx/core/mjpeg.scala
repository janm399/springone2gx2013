package org.eigengo.sogx.core

import java.util
import org.eigengo.sogx._
import java.util.Collections

class MJPEGDecoder {

  def decodeFrames(correlationId: CorrelationId, chunk: Chunk): util.Collection[ImageData] = {
    // Utils.writer.write(s"/Users/janmachacek/x$correlationId.mjpeg", chunk)
    Collections.singletonList(chunk.data)
  }

}
