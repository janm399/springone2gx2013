package org.eigengo.sogx.core

import com.xuggle.xuggler._
import javax.imageio.ImageIO
import java.io.{FileOutputStream, File, ByteArrayOutputStream}
import java.util.UUID
import java.util
import scala.collection._
import org.eigengo.sogx._

class H264Decoder {
  val sessions = mutable.HashMap[CorrelationId, H264DecoderContext]()

  def decodeFrames(correlationId: CorrelationId, chunk: Chunk): util.Collection[ImageData] = {
    val buffer = new util.ArrayList[ChunkData]()
    sessions.getOrElseUpdate(correlationId, new H264DecoderContext(correlationId)).decode(chunk.data)(buffer.add)
    buffer
  }

}

private[core] case class H264DecoderContext(correlationId: CorrelationId) {
  val container = IContainer.make()
  val tf: TemporaryFile = new TemporaryFile(correlationId)
  var isOpen = false
  var videoStream: IStream     = _
  var videoCoder: IStreamCoder = _

  def open(): Boolean = {
    if (!isOpen) {
      try {
        container.setReadRetryCount(0)
        container.open(tf.file.getAbsolutePath, IContainer.Type.READ, null)
        videoStream = container.getStream(0)
        videoCoder = videoStream.getStreamCoder

        videoCoder.open(IMetaData.make(), IMetaData.make())
        isOpen = true
      } catch {
        case x: Throwable => // noop
      }
    }
    isOpen
  }

  def decode[U](chunk: ChunkData)(f: ImageData => U): Unit = synchronized {
    tf.write(chunk)

    if (!open()) return

    val packet = IPacket.make()
    while (container.readNextPacket(packet) >= 0) {
      val picture = IVideoPicture.make(videoCoder.getPixelType, videoCoder.getWidth, videoCoder.getHeight)
      packet.getSize
      var offset = 0
      while (offset < packet.getSize) {
        val bytesDecoded = videoCoder.decodeVideo(picture, packet, offset)
        if (bytesDecoded > 0) {
          offset = offset + bytesDecoded
          if (picture.isComplete) {
            val javaImage = Utils.videoPictureToImage(picture)
            val os = new ByteArrayOutputStream
            ImageIO.write(javaImage, "png", os)
            os.close()
            f(os.toByteArray)
          }
        }
      }
    }
  }

  def close(): Unit = {
    tf.close()
    if (videoCoder != null) videoCoder.close()
  }

}

/**
 * Ghetto!
 */
private[core] class TemporaryFile(correlationId: CorrelationId) /* extends UtterlyMiserable */ {
  //val file: File = File.createTempFile("video", "mp4")
  val file: File = new File(s"/Users/janmachacek/$correlationId.mp4")
  file.deleteOnExit()
  var open: Boolean = true
  private val fos: FileOutputStream = new FileOutputStream(file, true)

  def write(buffer: Array[Byte]): Unit = {
    if (open) {
      fos.write(buffer)
      fos.flush()
    }
  }

  def close(): Unit = {
    if (open) {
      fos.close()
      file.delete()
      open = false
    }
  }

}
