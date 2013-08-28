package org.eigengo.sogx

case class Chunk(data: ChunkData, end: Boolean)

case class CorrelationId(value: String) extends AnyVal {
  override def toString = value
}

case class RecogSessionId(value: String) extends AnyVal {
  override def toString = value
}

object CorrelationId {
  implicit def toCorrelationId(value: String): CorrelationId = CorrelationId(value)
}
