package org.eigengo.sogx.config

import org.springframework.context.annotation.{Configuration, ComponentScan}
import org.springframework.web.servlet.config.annotation.{DefaultServletHandlerConfigurer, WebMvcConfigurerAdapter, EnableWebMvc}

@Configuration
@EnableWebMvc
@ComponentScan(basePackages=Array("org.eigengo.sogx"))
class Application extends WebMvcConfigurerAdapter with WebConfig {

  // Allow serving HTML files through the default Servlet
  override def configureDefaultServletHandling(configurer: DefaultServletHandlerConfigurer) = {
    configurer.enable()
  }


}
