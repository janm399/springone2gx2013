package org.eigengo.sogx

import com.fasterxml.jackson.annotation.{JsonProperty, JsonCreator}

case class CoinResponse @JsonCreator()(@JsonProperty(value = "coins", required = false) coins: Array[Coin],
                                       @JsonProperty(value = "succeeded") succeeded: Boolean)
case class Coin @JsonCreator() (@JsonProperty("center") center: Double, @JsonProperty("radius") radius: Double)
