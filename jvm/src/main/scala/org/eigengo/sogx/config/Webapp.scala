package org.eigengo.sogx.config

import org.springframework.context.annotation.{Bean, Configuration, ComponentScan}
import org.springframework.web.servlet.config.annotation.{DefaultServletHandlerConfigurer, WebMvcConfigurerAdapter, EnableWebMvc}
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
@EnableWebMvc
@ComponentScan(basePackages=Array("org.eigengo.sogx"))
class Webapp extends WebMvcConfigurerAdapter with WebConfig with CoreConfig {

  @Bean def asyncExecutor(): ThreadPoolTaskExecutor = {
    val executor: ThreadPoolTaskExecutor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(4)
    executor.setCorePoolSize(8)
    executor.setThreadNamePrefix("MessageChannel-")
    executor
  }

  // Allow serving HTML files through the default Servlet
  override def configureDefaultServletHandling(configurer: DefaultServletHandlerConfigurer) = {
    configurer.enable()
  }

}
