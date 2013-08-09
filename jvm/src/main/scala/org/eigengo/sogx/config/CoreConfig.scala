package org.eigengo.sogx.config

import org.springframework.messaging.SubscribableChannel
import org.springframework.messaging.support.channel.ExecutorSubscribableChannel
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.{ThreadPoolTaskScheduler, ThreadPoolTaskExecutor}
import org.springframework.messaging.simp.{SimpMessagingTemplate, SimpMessageSendingOperations}
import org.springframework.messaging.support.converter.MappingJackson2MessageConverter
import org.eigengo.sogx.core.RecogService

trait CoreConfig {
  val messageConverter = new MappingJackson2MessageConverter()

  @Bean def asyncExecutor(): ThreadPoolTaskExecutor = {
    val executor: ThreadPoolTaskExecutor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(4)
    executor.setCorePoolSize(8)
    executor.setThreadNamePrefix("MessageChannel-")
    executor
  }

  @Bean def dispatchChannel(): SubscribableChannel = {
     new ExecutorSubscribableChannel(asyncExecutor())
  }

  // MessagingTemplate (and MessageChannel) to dispatch messages to for further processing
  // All MessageHandler beans above subscribe to this channel
  @Bean
  def dispatchMessagingTemplate(): SimpMessageSendingOperations = {
    val template = new SimpMessagingTemplate(dispatchChannel())
    template.setMessageConverter(messageConverter)
    template
  }

  // Task executor for use in SockJS (heartbeat frames, session timeouts)
  @Bean
  def taskScheduler(): ThreadPoolTaskScheduler = {
    val taskScheduler = new ThreadPoolTaskScheduler()
    taskScheduler.setThreadNamePrefix("SockJS-")
    taskScheduler.setPoolSize(4)
    taskScheduler
  }

  // services
  @Bean
  def recogService(): RecogService = {
    val service = new RecogService(dispatchMessagingTemplate())
    taskScheduler().scheduleAtFixedRate(new Runnable {
      def run() {
        service.sendQuotes()
      }
    }, 1000L)

    service
  }

}
