package org.eigengo.sogx.config

import org.springframework.messaging.simp.handler.{UserDestinationMessageHandler, SimpleBrokerMessageHandler, AnnotationMethodMessageHandler, SimpleUserQueueSuffixResolver}
import org.springframework.web.socket.WebSocketHandler
import org.springframework.context.annotation.{Profile, Bean}
import org.springframework.messaging.simp.stomp.{StompProtocolHandler, StompBrokerRelayMessageHandler}
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping
import java.util.Collections
import java.util
import org.springframework.messaging.SubscribableChannel
import org.springframework.messaging.support.channel.ExecutorSubscribableChannel
import org.springframework.web.socket.sockjs.transport.handler.DefaultSockJsService
import org.springframework.web.socket.sockjs.SockJsHttpRequestHandler
import org.springframework.messaging.handler.websocket.SubProtocolWebSocketHandler

trait WebConfig extends CoreConfig {
  val userQueueSuffixResolver = new SimpleUserQueueSuffixResolver()

  @Bean
  def handlerMapping(): SimpleUrlHandlerMapping = {
    val sockJsService = new DefaultSockJsService(taskScheduler())
    val requestHandler = new SockJsHttpRequestHandler(sockJsService, webSocketHandler())

    val hm = new SimpleUrlHandlerMapping()
    hm.setOrder(-1)
    hm.setUrlMap(Collections.singletonMap("/ws/**", requestHandler))

    hm
  }

  // WebSocketHandler supporting STOMP messages
  @Bean
  def webSocketHandler(): WebSocketHandler = {
    val stompHandler = new StompProtocolHandler()
    stompHandler.setUserQueueSuffixResolver(userQueueSuffixResolver)

    val webSocketHandler = new SubProtocolWebSocketHandler(dispatchChannel())
    webSocketHandler.setDefaultProtocolHandler(stompHandler)
    webSocketHandlerChannel().subscribe(webSocketHandler)

    webSocketHandler
  }

  // MessageHandler for processing messages by delegating to @Controller annotated methods
  @Bean
  def annotationMessageHandler(): AnnotationMethodMessageHandler = {
    val handler = new AnnotationMethodMessageHandler(dispatchMessagingTemplate(), webSocketHandlerChannel());

    handler.setDestinationPrefixes(util.Arrays.asList("/app/"))
    handler.setMessageConverter(messageConverter)
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
    dispatchChannel().subscribe(handler)
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
    val handler = new UserDestinationMessageHandler(dispatchMessagingTemplate(), userQueueSuffixResolver)
    dispatchChannel().subscribe(handler)
    handler
  }

  // Channel for sending STOMP messages to connected WebSocket sessions (mostly for internal use)
  @Bean
  def webSocketHandlerChannel(): SubscribableChannel = {
    new ExecutorSubscribableChannel(asyncExecutor())
  }

}
