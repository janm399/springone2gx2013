package org.eigengo.sogx.core

import org.springframework.integration.annotation.Gateway
import org.eigengo.sogx.CoinResponse

trait RecogGateway {

  @Gateway(requestChannel = "recogRequest", replyChannel = "recogResponse", replyTimeout = 1000)
  def recogFrame(frame: Array[Byte]): CoinResponse

}
