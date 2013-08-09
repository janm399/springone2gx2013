package org.eigengo.sogx.config

import org.springframework.context.annotation.{Configuration, ComponentScan}
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
@ComponentScan(basePackages=Array("org.eigengo.sogx"))
class Application extends WebConfig
