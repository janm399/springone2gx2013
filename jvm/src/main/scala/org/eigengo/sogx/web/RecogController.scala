package org.eigengo.sogx.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.eigengo.sogx.{Chunk, Coin}
import org.springframework.beans.factory.annotation.Autowired
import org.eigengo.sogx.core.RecogService
import org.eigengo.sogx._
import java.util.UUID
import org.eigengo.sogx.cli.Utils
import org.eigengo.sogx.Chunk

@Controller
class RecogController @Autowired()(recogService: RecogService) {
  val globalId = UUID.randomUUID()

  @RequestMapping(value = Array("/app/recog"), method = Array(RequestMethod.POST))
  @ResponseBody
  def begin(): String = {
    UUID.randomUUID().toString
  }

  @RequestMapping(value = Array("/app/recog/h264/{correlationId}"), method = Array(RequestMethod.POST))
  def h264Chunk(@PathVariable correlationId: CorrelationId, @RequestBody body: Array[Byte]): Unit = {
    recogService.h264Chunk(globalId, Chunk(body, true))
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
