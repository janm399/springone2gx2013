package org.eigengo.sogx.core

import org.springframework.integration.annotation.Gateway

trait RecogService {

  @Gateway(requestChannel = "recogRequest", replyChannel = "recogResponse", replyTimeout = 1000)
  def recogFrame(frame: Array[Byte]): CoinResponse

}
