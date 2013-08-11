package org.eigengo.sogx.core

import org.springframework.messaging.core.MessageSendingOperations
import org.springframework.scheduling.annotation.Scheduled
import org.eigengo.sogx.{ContentTypes, CoinResponse, Coin}
import java.util.UUID
import scala.util.Random
import org.springframework.integration.MessageChannel
import org.springframework.integration.support.MessageBuilder
import ContentTypes._

class RecogService(recogChannel: MessageChannel, messagingTemplate: MessageSendingOperations[String]) {
  val random = new Random()

  @Scheduled(fixedDelay = 1000)
  def dummy(): Unit = {
    val width = 800
    val height = 600
    val count = random.nextInt(10)

    val coinResponse = CoinResponse((0 to count).map(_ => Coin((random.nextInt(width), random.nextInt(height)), random.nextInt(100))).toArray, true)
    messagingTemplate.convertAndSend(s"/topic/recog/coin.${UUID.randomUUID()}", coinResponse)
  }

  def image(session: UUID, chunk: Array[Byte]) = {
    val message = MessageBuilder.
      withPayload(chunk).
      setCorrelationId(session).
      setHeader("content-type", `image/*`).
      build()

    recogChannel.send(message)
  }

  def h264Frame(session: UUID, frame: Array[Byte]) = {
    val message = MessageBuilder.
      withPayload(frame).
      setCorrelationId(session).
      setHeader("content-type", `video/h264`).
      build()

    recogChannel.send(message)
  }

}
