package org.eigengo.sogx.core

case class CoinResponse(coins: Array[Coin], succeeded: Boolean)
case class Coin(center: Double, radius: Double)
