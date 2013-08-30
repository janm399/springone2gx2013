package org.eigengo.sogx.core

import org.eigengo.sogx._
import org.springframework.integration.annotation.{Payload, Header}

/**
 * Implementations implement the ``onCoinResponse`` method to handle the coin responses.
 */
trait RecogServiceActivator {

  /**
   * Called when a frame has been successfully processed by the vision components
   *
   * @param correlationId the correlation id
   * @param coins the response
   */
  def onCoinResponse(@Header correlationId: CorrelationId, @Payload coins: CoinResponse): Unit

}
