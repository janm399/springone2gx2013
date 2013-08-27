package org.eigengo.sogx.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import org.eigengo.sogx.core.RecogService
import org.eigengo.sogx._
import java.util.UUID
import org.eigengo.sogx.Chunk
import org.springframework.messaging.handler.annotation.{SessionId, MessageBody, MessageMapping}

@Controller
class RecogController @Autowired()(recogService: RecogService) {

  @MessageMapping(Array("/app/recog/h264"))
  def h264(@SessionId sessionId: CorrelationId, @MessageBody body: ChunkData): Unit = {
    recogService.h264Chunk(sessionId, Chunk(body, true))
  }

  @MessageMapping(Array("/app/recog/image"))
  def image(@SessionId sessionId: CorrelationId, @MessageBody body: ChunkData): Unit = {
    recogService.imageChunk(sessionId, Chunk(body, true))
  }

  @MessageMapping(Array("/app/recog/mjpeg"))
  def mjpeg(@SessionId sessionId: CorrelationId, @MessageBody body: ChunkData): Unit = {
    recogService.mjpegChunk(sessionId, Chunk(body, true))
  }

  @RequestMapping(Array("/app/foo"))
  @ResponseBody
  def foo(): String = {
    Utils.reader.readAll("/coins2.png")(recogService.imageChunk(UUID.randomUUID().toString, _))
    "Hello, world"
  }

  @RequestMapping(Array("/app/bar"))
  @ResponseBody
  def bar(@RequestParam(defaultValue = "64") bps: Int): String = {
    val id = UUID.randomUUID().toString
    Utils.reader.readChunks("/coins.mjpeg", bps)(recogService.h264Chunk(id, _))
    "Hello, world"
  }

}
