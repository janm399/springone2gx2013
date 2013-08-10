package org.eigengo.sogx

import com.fasterxml.jackson.annotation.{JsonProperty, JsonCreator}
import scala.beans.{BeanInfo, BeanProperty}

case class CoinResponse @JsonCreator()(@JsonProperty(value = "coins", required = false) @BeanProperty coins: Array[Coin],
                                       @JsonProperty(value = "succeeded") @BeanProperty succeeded: Boolean)
case class Coin @JsonCreator() (@JsonProperty("center") @BeanProperty center: Point,
                                @JsonProperty("radius") @BeanProperty radius: Int)
case class Point @JsonCreator() (@JsonProperty("x") @BeanProperty x: Int,
                                 @JsonProperty("y") @BeanProperty y: Int)

object Point {

  implicit def toPoint(point: (Int, Int)) = Point(point._1, point._2)

}