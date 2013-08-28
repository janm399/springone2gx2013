package org.eigengo.sogx.config

import org.springframework.messaging.support.channel.ExecutorSubscribableChannel
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.messaging.simp.{SimpMessagingTemplate, SimpMessageSendingOperations}
import org.springframework.messaging.support.converter.MappingJackson2MessageConverter
import org.eigengo.sogx.core._
import java.util.concurrent.Executor
import org.springframework.integration.channel.DirectChannel
import org.springframework.scheduling.TaskScheduler

trait CoreConfig {
  // the message converter for the payloads
  val messageConverter = new MappingJackson2MessageConverter()

  // implementations must provide appropriate Executor
  @Bean def asyncExecutor(): Executor

  // implementations must provide RecogServiceActivator, which will be executed when we have the coins from the
  // video frames
  @Bean def recogServiceActivator(): RecogServiceActivator

  @Bean def mjpegDecoder() = new MJPEGDecoder()

  // Decodes incoming chunks into frames that can be sent to RMQ
  @Bean def chunkDecoder() = new ChunkDecoder(mjpegDecoder())

  // the channel onto which the requests will go
  @Bean def recogRequest() = new DirectChannel()

  // the channel that connects to the WS clients
  @Bean def dispatchChannel() = new ExecutorSubscribableChannel(asyncExecutor())

  // MessagingTemplate (and MessageChannel) to dispatch messages to for further processing
  // All MessageHandler beans above subscribe to this channel
  @Bean def dispatchMessagingTemplate(): SimpMessageSendingOperations = {
    val template = new SimpMessagingTemplate(dispatchChannel())
    template.setMessageConverter(messageConverter)
    template
  }

  // Task executor for use in SockJS (heartbeat frames, correlationId timeouts)
  @Bean def taskScheduler(): TaskScheduler = {
    val taskScheduler = new ThreadPoolTaskScheduler()
    taskScheduler.setThreadNamePrefix("SockJS-")
    taskScheduler.setPoolSize(4)
    taskScheduler
  }

  // services
  @Bean def recogService(): RecogService = new RecogService(recogRequest(), dispatchMessagingTemplate())

  @Bean def recogSessions() = new RecogSessions(dispatchMessagingTemplate())

}
