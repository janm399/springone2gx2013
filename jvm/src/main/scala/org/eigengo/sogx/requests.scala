package org.eigengo.sogx

/**
 * CorrelationId ties together messages that are sent and received from RabbitMQ.
 *
 * CorrelationId is a new type (without the boxing) that carries the ``String``, but that
 * ``String`` has a special and unchangeable meaning.
 *
 * @param value the underlying value
 */
case class CorrelationId(value: String) extends AnyVal {
  override def toString = value
}

/**
 * For convenience, we provide an implicit conversions wherever ``CorrelationId`` is used (viz the Scala implicit scope)
 */
object CorrelationId {
  /**
   * Conveniently converts a ``String`` into the ``CorrelationId``
   * @param value the string to be converted
   * @return the CorrelationId with that string
   */
  implicit def toCorrelationId(value: String): CorrelationId = CorrelationId(value)
}

/**
 * RecogSessionId maintains identity of the open recognition sessions, where the value holds
 * the underlying identity. This may be a websocket id, a generated UUID, or any other unique-enough
 * value.
 *
 * @param value the underlying session identity
 */
case class RecogSessionId(value: String) extends AnyVal {
  override def toString = value
}

