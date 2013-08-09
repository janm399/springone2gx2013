package org.eigengo.sogx

import com.fasterxml.jackson.annotation.{JsonProperty, JsonCreator}
import scala.beans.BeanProperty

@BeanProperty
case class CoinResponse @JsonCreator()(@JsonProperty(value = "coins", required = false) coins: Array[Coin],
                                       @JsonProperty(value = "succeeded") succeeded: Boolean)
@BeanProperty
case class Coin @JsonCreator() (@JsonProperty("center") center: Double, @JsonProperty("radius") radius: Double)
