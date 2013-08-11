package org.eigengo.sogx.config

import org.springframework.context.annotation.Bean
import org.springframework.integration.MessageChannel
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.gateway.GatewayProxyFactoryBean
import org.springframework.integration.config.SpringIntegrationConfigurer
import org.eigengo.sogx.core.RecogGateway
import java.util.concurrent.Executor

trait IntegrationConfig extends SpringIntegrationConfigurer {
  def asyncExecutor(): Executor

  @Bean
  def recogRequest(): MessageChannel = {
    new DirectChannel()
  }

  @Bean
  def recogResponse(): MessageChannel = {
    new DirectChannel()
  }

  @Bean
  def recogGateway(): GatewayProxyFactoryBean =  {
    gatewayProxy[RecogGateway].withAsyncExecutor(asyncExecutor())
  }
}
