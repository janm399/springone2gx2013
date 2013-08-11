package org.eigengo.sogx.core

import com.xuggle.xuggler._
import javax.imageio.ImageIO
import java.io.{FileOutputStream, File, ByteArrayOutputStream}
import java.util.UUID
import java.util

object H264Decoder {

  def decodeFrames(session: UUID, chunk: Array[Byte]): util.Collection[Array[Byte]] = {
    val tf = new TemporaryFile(session)
    val buffer: util.List[Array[Byte]] = new util.ArrayList[Array[Byte]]()
    val dc = new H264DecoderContext(tf)(buffer.add)
    dc.decode(chunk)
    buffer
  }

}

/**
 * Decodes the incoming chunks of H.264 stream; applies the function ``f`` to all
 * completed frames in the stream.
 *
 * It is not to be shared by multiple threads.
 *
 * @param f the function to be applied to every decoded frame
 * @tparam U return type
 */
class H264DecoderContext[U](tf: TemporaryFile)(f: Array[Byte] => U) {
  val container = IContainer.make()

  var isOpen = false
  var videoStream: IStream     = _
  var videoCoder: IStreamCoder = _

  def open(): Boolean = {
    if (!isOpen) {
      try {
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

  def decode(chunk: Array[Byte]): Unit = {
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
private[core] class TemporaryFile(session: UUID) /* extends UtterlyMiserable */ {
  //val file: File = File.createTempFile("video", "mp4")
  val file: File = new File("/Users/janmachacek/x.mp4")
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
