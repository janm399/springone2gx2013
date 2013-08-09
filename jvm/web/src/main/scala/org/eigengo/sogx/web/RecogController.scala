package org.eigengo.sogx.web

import org.springframework.stereotype.Controller
import org.eigengo.sogx.core.Coin
import org.springframework.messaging.simp.annotation.SubscribeEvent
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}

@Controller
class RecogController {

  @SubscribeEvent(Array("/app/coins"))
  def coins(): Array[Coin] = {
    val count = (math.random * 10).toInt

    (0 to count).map(_ => Coin(math.random, math.random)).toArray
  }

  @RequestMapping(Array("/app/foo"))
  @ResponseBody
  def index(): String = {
    "Hello, world"
  }

}
