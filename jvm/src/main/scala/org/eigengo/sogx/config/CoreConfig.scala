package org.eigengo.sogx.config

import org.springframework.messaging.SubscribableChannel
import org.springframework.messaging.support.channel.ExecutorSubscribableChannel
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

trait CoreConfig {

  @Bean def asyncExecutor(): ThreadPoolTaskExecutor = {
    val executor: ThreadPoolTaskExecutor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(4)
    executor.setCorePoolSize(8)
    executor.setThreadNamePrefix("MessageChannel-")
    executor
  }

  @Bean def dispatchChannel(): SubscribableChannel = {
     new ExecutorSubscribableChannel(asyncExecutor)
  }
}
