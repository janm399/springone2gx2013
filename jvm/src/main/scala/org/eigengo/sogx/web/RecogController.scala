package org.eigengo.sogx.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import org.eigengo.sogx.core.{RecogSessions, RecogService}
import org.eigengo.sogx._
import java.util.UUID
import org.springframework.messaging.handler.annotation.{SessionId, MessageBody, MessageMapping}

@Controller
class RecogController @Autowired()(recogService: RecogService, recogSessions: RecogSessions) {

  @MessageMapping(Array("/app/recog/image"))
  def image(@SessionId sessionId: CorrelationId, @MessageBody body: ChunkData): Unit = {
    recogService.imageChunk(sessionId, body)
  }

  @MessageMapping(Array("/app/recog/mjpeg"))
  def mjpeg(@SessionId sessionId: CorrelationId, @MessageBody body: ChunkData): Unit = {
    recogService.mjpegChunk(sessionId, body)
  }

  @RequestMapping(Array("/app/predef/image"))
  @ResponseBody
  def foo(): String = {
    val id = UUID.randomUUID().toString
    Utils.reader.readAll("/coins2.png")(recogService.imageChunk(id, _))
    recogSessions.sessionEnded(RecogSessionId(id))
    "Image"
  }

  @RequestMapping(Array("/app/predef/coins"))
  @ResponseBody
  def bar(@RequestParam(defaultValue = "10") fps: Int): String = {
    val id = UUID.randomUUID().toString
    Utils.reader.readChunks("/coins2.mjpeg", fps)(recogService.mjpegChunk(id, _))
    recogSessions.sessionEnded(RecogSessionId(id))
    "coins"
  }

}
