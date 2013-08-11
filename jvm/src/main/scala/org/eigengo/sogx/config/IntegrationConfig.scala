package org.eigengo.sogx.config

import org.springframework.context.annotation.Bean
import org.springframework.integration.MessageChannel
import org.springframework.integration.gateway.GatewayProxyFactoryBean
import org.springframework.integration.config.SpringIntegration
import org.eigengo.sogx.core.RecogGateway
import java.util.concurrent.Executor

trait IntegrationConfig {
  import SpringIntegration.channels._
  import SpringIntegration.gateways._

  def asyncExecutor(): Executor

  @Bean
  def recogRequest(): MessageChannel = directChannel()

  @Bean
  def recogResponse(): MessageChannel = directChannel()

  @Bean
  def rawRecogResponse(): MessageChannel = directChannel()

  @Bean
  def rawBytesRecogResponse(): MessageChannel = directChannel()

  @Bean
  def recogGateway(): GatewayProxyFactoryBean = {
    gatewayProxy[RecogGateway].withAsyncExecutor(asyncExecutor())
  }
}
