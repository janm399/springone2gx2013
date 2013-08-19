package org.eigengo.sogx.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import org.eigengo.sogx.core.RecogService
import org.eigengo.sogx._
import java.util.UUID
import org.eigengo.sogx.cli.Utils
import org.eigengo.sogx.Chunk
import org.springframework.messaging.handler.annotation.{SessionId, MessageBody, MessageMapping}

/**
 * cat coins.mp4 | curl --limit-rate 64k -T - -X POST http://localhost:8080/app/recog/h264/`uuidgen`
 * cat coins.mp4 | curl --limit-rate 64k -v --header "Transfer-Encoding: chunked" -T - -X POST http://localhost:8080/app/recog/h264/`uuidgen`
 *
 *
 * @param recogService
 */
@Controller
class RecogController @Autowired()(recogService: RecogService) {
  val globalId = UUID.randomUUID()

  @RequestMapping(value = Array("/app/recog"), method = Array(RequestMethod.POST))
  @ResponseBody
  def begin(): String = {
    UUID.randomUUID().toString
  }

  @RequestMapping(value = Array("/app/recog/fullh264/{correlationId}"), method = Array(RequestMethod.POST))
  @ResponseBody
  def fullH264(@PathVariable correlationId: CorrelationId, @RequestBody body: ChunkData): Unit = {
    recogService.h264Chunk(correlationId, Chunk(body, true))
  }

  @MessageMapping(Array("/app/recog/h264"))
  def h264Chunk(@SessionId sessionId: String, @MessageBody body: ChunkData): Unit = {
    // TODO: replace globalId with @MessageHeader attribute
    recogService.h264Chunk(globalId, Chunk(body, true))
  }

  @RequestMapping(value = Array("/app/recog/fullimage/{correlationId}"), method = Array(RequestMethod.POST))
  @ResponseBody
  def fullImage(@PathVariable correlationId: CorrelationId, @RequestBody body: ChunkData): Unit = {
    recogService.imageChunk(correlationId, Chunk(body, true))
  }

  @RequestMapping(Array("/app/foo"))
  @ResponseBody
  def foo(): String = {
    Utils.readAll("/coins2.png")(recogService.imageChunk(UUID.randomUUID(), _))
    "Hello, world"
  }

  @RequestMapping(Array("/app/bar"))
  @ResponseBody
  def bar(@RequestParam(defaultValue = "64") bps: Int): String = {
    val id = UUID.randomUUID()
    Utils.readChunks("/coins.mp4", bps)(recogService.h264Chunk(id, _))
    "Hello, world"
  }

}
