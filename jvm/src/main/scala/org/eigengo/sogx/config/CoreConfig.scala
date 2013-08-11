package org.eigengo.sogx.config

import org.springframework.messaging.SubscribableChannel
import org.springframework.messaging.support.channel.ExecutorSubscribableChannel
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.messaging.simp.{SimpMessagingTemplate, SimpMessageSendingOperations}
import org.springframework.messaging.support.converter.MappingJackson2MessageConverter
import org.eigengo.sogx.core.{RecogServiceActivator, RecogService}
import java.util.concurrent.Executor
import org.springframework.integration.MessageChannel
import org.springframework.integration.channel.DirectChannel

trait CoreConfig {
  val messageConverter = new MappingJackson2MessageConverter()

  // implementations must provide appropriate Executor
  @Bean def asyncExecutor(): Executor

  // implementations must provide RecogServiceActivator, which will be executed when we have the coins from the
  // video frames
  @Bean def recogServiceActivator(): RecogServiceActivator

  // the channel onto which the requests will go
  @Bean def recogRequest(): MessageChannel = new DirectChannel()

  // the channel that connects to the WS clients
  @Bean def dispatchChannel(): SubscribableChannel = {
     new ExecutorSubscribableChannel(asyncExecutor())
  }

  // MessagingTemplate (and MessageChannel) to dispatch messages to for further processing
  // All MessageHandler beans above subscribe to this channel
  @Bean def dispatchMessagingTemplate(): SimpMessageSendingOperations = {
    val template = new SimpMessagingTemplate(dispatchChannel())
    template.setMessageConverter(messageConverter)
    template
  }

  // Task executor for use in SockJS (heartbeat frames, session timeouts)
  @Bean def taskScheduler(): ThreadPoolTaskScheduler = {
    val taskScheduler = new ThreadPoolTaskScheduler()
    taskScheduler.setThreadNamePrefix("SockJS-")
    taskScheduler.setPoolSize(4)
    taskScheduler
  }

  // services
  @Bean def recogService(): RecogService = {
    new RecogService(recogRequest(), dispatchMessagingTemplate())
  }

}
