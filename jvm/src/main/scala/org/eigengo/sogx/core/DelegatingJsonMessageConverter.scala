package org.eigengo.sogx.core

import org.springframework.messaging.support.converter.MessageConverter
import org.springframework.messaging.support.MessageBuilder
import org.springframework.messaging.Message
import java.lang.reflect.Type

class DelegatingJsonMessageConverter(otherConverter: MessageConverter[Object]) extends MessageConverter[Object] {

  private def toMessage0(payload: Object): Message[_] = payload match {
    case s: String                  => MessageBuilder.withPayload(s.getBytes).build()
    case x                          => otherConverter.toMessage(x)
  }

  def toMessage[P](payload: Object): Message[P] = toMessage0(payload).asInstanceOf[Message[P]]

  def fromMessage(message: Message[_], targetClass: Type): Object = message.getPayload.asInstanceOf[Object]

}
