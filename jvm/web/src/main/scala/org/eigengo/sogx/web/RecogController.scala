package org.eigengo.sogx.web

import org.springframework.stereotype.Controller
import org.eigengo.sogx.core.Coin
import org.springframework.messaging.simp.annotation.SubscribeEvent

@Controller
class RecogController {

  @SubscribeEvent(Array("/coins"))
  def coins(): Array[Coin] = {
    val count = (math.random * 10).toInt

    (0 to count).map(_ => Coin(math.random, math.random)).toArray
  }

}
