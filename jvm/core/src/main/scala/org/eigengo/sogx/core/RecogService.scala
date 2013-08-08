package org.eigengo.sogx.core

import org.springframework.integration.annotation.Gateway


trait RecogService {

  @Gateway(requestChannel = "recogRequest", replyChannel = "recogResponse")
  def recogFrame(frame: Array[Byte]): Array[Byte]

}
