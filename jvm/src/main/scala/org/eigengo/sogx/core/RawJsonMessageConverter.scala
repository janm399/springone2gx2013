package org.eigengo.sogx.core

import org.springframework.messaging.support.converter.MessageConverter
import org.springframework.messaging.support.MessageBuilder
import org.springframework.messaging.Message
import java.lang.reflect.Type
import java.util
import scala.collection.JavaConversions

class RawJsonMessageConverter extends MessageConverter[Object] {
  import JavaConversions._

  def toMessage[P](payload: Object): Message[P] = payload match {
    case s: String             => MessageBuilder.withPayload(s.getBytes.asInstanceOf[P]).build()
    case b: Array[Byte]        => MessageBuilder.withPayload(b.asInstanceOf[P]).build()

    case c: util.Collection[_] =>
      val b = new StringBuilder
      b.append("[")
      c.foreach(s => if (b.length == 1) b.append(s) else {b.append(","); b.append(s)})
      b.append("]")
      MessageBuilder.withPayload(b.toString().getBytes.asInstanceOf[P]).build()

    case x                     => toMessage(x.toString)
  }

  def fromMessage(message: Message[_], targetClass: Type): Object = message.getPayload.asInstanceOf[Object]
}
