package org.eigengo.sogx.core

import org.eigengo.sogx.CoinResponse
import org.springframework.integration.annotation.{Payload, Header}
import java.util.UUID

trait RecogServiceActivator {

  def onCoinResponse(@Header correlationId: UUID, @Payload coins: CoinResponse): Unit

}
