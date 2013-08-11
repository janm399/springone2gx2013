package org.eigengo.sogx.web

import org.springframework.stereotype.Controller
import org.springframework.messaging.simp.annotation.SubscribeEvent
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.eigengo.sogx.Coin
import org.springframework.beans.factory.annotation.Autowired
import org.eigengo.sogx.core.RecogService
import java.util.UUID
import org.eigengo.sogx.cli.Utils

@Controller
class RecogController @Autowired()(recogService: RecogService) {
/*
  @SubscribeEvent(Array("/app/coins"))
  def coins(): Array[Coin] = {
    val count = (math.random * 10).toInt

    (0 to count).map(_ => Coin(math.random, math.random)).toArray
  }
*/
  
  @RequestMapping(Array("/app/foo"))
  @ResponseBody
  def index(): String = {
    Utils.readAll("/coins2.png")(recogService.image(UUID.randomUUID(), _))
    "Hello, world"
  }

}
