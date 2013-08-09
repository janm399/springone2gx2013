package org.eigengo.sogx.config

import org.springframework.web.servlet.config.annotation.{DefaultServletHandlerConfigurer, WebMvcConfigurerAdapter}
import org.springframework.messaging.support.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.handler.{UserDestinationMessageHandler, SimpleBrokerMessageHandler, AnnotationMethodMessageHandler, SimpleUserQueueSuffixResolver}
import org.springframework.web.socket.WebSocketHandler
import org.springframework.context.annotation.{Profile, Bean}
import org.springframework.messaging.simp.stomp.{StompBrokerRelayMessageHandler, StompWebSocketHandler}
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping
import org.springframework.web.socket.sockjs.support.{SockJsHttpRequestHandler, DefaultSockJsService}
import java.util.Collections
import java.util
import org.springframework.messaging.simp.{SimpMessagingTemplate, SimpMessageSendingOperations}
import org.springframework.messaging.SubscribableChannel
import org.springframework.messaging.support.channel.ExecutorSubscribableChannel
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

trait WebConfig extends WebMvcConfigurerAdapter with CoreConfig {
  val messageConverter = new MappingJackson2MessageConverter()
  val userQueueSuffixResolver = new SimpleUserQueueSuffixResolver()

  @Bean
  def handlerMapping(): SimpleUrlHandlerMapping = {
    val sockJsService = new DefaultSockJsService(taskScheduler())
    val requestHandler = new SockJsHttpRequestHandler(sockJsService, webSocketHandler())

    val hm = new SimpleUrlHandlerMapping()
    hm.setOrder(-1)
    hm.setUrlMap(Collections.singletonMap("/sogx/**", requestHandler))

    hm
  }

  // WebSocketHandler supporting STOMP messages
  @Bean
  def webSocketHandler(): WebSocketHandler = {
    new StompWebSocketHandler(dispatchChannel())
  }

  // MessageHandler for processing messages by delegating to @Controller annotated methods
  @Bean
  def annotationMessageHandler(): AnnotationMethodMessageHandler = {
    val handler = new AnnotationMethodMessageHandler(dispatchMessagingTemplate(), webSocketHandlerChannel());

    handler.setDestinationPrefixes(util.Arrays.asList("/app/"))
    handler.setMessageConverter(this.messageConverter)
    dispatchChannel().subscribe(handler)
    handler
  }

  // MessageHandler that acts as a "simple" message broker
  // See DispatcherServletInitializer for enabling/disabling the "simple-broker" profile
  @Bean
  @Profile(Array("simple-broker"))
  def simpleBrokerMessageHandler(): SimpleBrokerMessageHandler = {
    val handler = new SimpleBrokerMessageHandler(webSocketHandlerChannel())
    handler.setDestinationPrefixes(util.Arrays.asList("/topic/", "/queue/"))
    dispatchChannel().subscribe(handler);
    handler
  }

  // MessageHandler that relays messages to and from external STOMP broker
  // See DispatcherServletInitializer for enabling/disabling the "stomp-broker-relay" profile
  @Bean
  @Profile(Array("stomp-broker-relay"))
  def stompBrokerRelayMessageHandler(): StompBrokerRelayMessageHandler = {
    val handler = new StompBrokerRelayMessageHandler(
      webSocketHandlerChannel(), util.Arrays.asList("/topic/", "/queue/"))

    dispatchChannel().subscribe(handler)
    handler
  }

  // MessageHandler that resolves destinations prefixed with "/user/{user}"
  // See the Javadoc of UserDestinationMessageHandler for details
  @Bean
  def userMessageHandler(): UserDestinationMessageHandler = {
    val handler = new UserDestinationMessageHandler(
      dispatchMessagingTemplate(), this.userQueueSuffixResolver)
    dispatchChannel().subscribe(handler)
    handler
  }


  // MessagingTemplate (and MessageChannel) to dispatch messages to for further processing
  // All MessageHandler beans above subscribe to this channel
  @Bean
  def dispatchMessagingTemplate(): SimpMessageSendingOperations = {
    val template = new SimpMessagingTemplate(dispatchChannel())
    template.setMessageConverter(this.messageConverter)
    template
  }

  // Channel for sending STOMP messages to connected WebSocket sessions (mostly for internal use)
  @Bean
  def webSocketHandlerChannel(): SubscribableChannel = {
    new ExecutorSubscribableChannel(asyncExecutor())
  }

  // Task executor for use in SockJS (heartbeat frames, session timeouts)
  @Bean
  def taskScheduler(): ThreadPoolTaskScheduler = {
    val taskScheduler = new ThreadPoolTaskScheduler()
    taskScheduler.setThreadNamePrefix("SockJS-")
    taskScheduler.setPoolSize(4)
    taskScheduler
  }

  // Allow serving HTML files through the default Servlet
  override def configureDefaultServletHandling(configurer: DefaultServletHandlerConfigurer) = {
    configurer.enable()
  }

}
