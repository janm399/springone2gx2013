package org.eigengo.sogx.web

import org.springframework.stereotype.Controller
import org.springframework.messaging.simp.annotation.SubscribeEvent
import org.springframework.web.bind.annotation.{RequestParam, ResponseBody, RequestMapping}
import org.eigengo.sogx.Coin
import org.springframework.beans.factory.annotation.Autowired
import org.eigengo.sogx.core.RecogService
import java.util.UUID
import org.eigengo.sogx.cli.Utils

@Controller
class RecogController @Autowired()(recogService: RecogService) {

  @RequestMapping(Array("/app/foo"))
  @ResponseBody
  def foo(): String = {
    Utils.readAll("/coins2.png")(recogService.image(UUID.randomUUID(), _))
    "Hello, world"
  }

  @RequestMapping(Array("/app/bar"))
  @ResponseBody
  def bar(@RequestParam(defaultValue = "64") bps: Int): String = {
    val id = UUID.randomUUID()
    Utils.readChunks("/coins.mp4", bps)(recogService.h264Frame(id, _))
    "Hello, world"
  }

}
