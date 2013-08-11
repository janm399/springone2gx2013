package org.springframework.integration.config

import org.springframework.integration.MessageChannel
import org.springframework.integration.channel.DirectChannel

trait Channels {

  def directChannel(): MessageChannel = new DirectChannel()

}
