package org.eigengo.sogx.config

import org.springframework.messaging.simp.handler.{UserDestinationMessageHandler, SimpleBrokerMessageHandler, AnnotationMethodMessageHandler, SimpleUserQueueSuffixResolver}
import org.springframework.web.socket.{CloseStatus, WebSocketSession, WebSocketHandler}
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
import org.springframework.web.socket.server.support.WebSocketHttpRequestHandler
import org.springframework.messaging.handler.annotation.support.SessionIdMehtodArgumentResolver
import org.springframework.messaging.handler.MessagingWebSocketHandler
import org.eigengo.sogx.RecogSessionId
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

/**
 * Contains the components that make up the web application. We require that it is mixed in with the
 * ``CoreConfig``. It would be wrong to say ``extends``, because ``WebConfig`` _is not_ some special
 * ``CoreConfig``: it merely requires it.
 *
 * It configures several URLs:
 *
 * HTTP /app/       ~> the Spring MVC machinery
 * HTTP /sockjs/    ~> the upgrade / change protocol endpoint
 * HTTP /websocket/ ~> the upgrade / change protocol endpoint
 * WS   /sockjs/    ~> the SockJS machinery, configured with the STOMP sub-protocol
 * WS   /websocket/ ~> the raw WebSocket machinery
 *
 * The ``DispatcherServlet`` machinery is the usual stuff of Spring MVC; the interesting portion is the
 * websocket stuff. I use the raw websocket support to receive fire-end-forget messages sent from the iOS
 * application. The SockJS support, at the highest level, is similar. But the SockJS support brings complete
 * full-duplex handler; handler that understands the structure of the received and sent messages.
 *
 * And so, in technical detail, we have:
 *
 * HTTP /app/       ~> DispatcherServletInitializer.getServletMappings
 * HTTP /sockjs/    ~> sockJsHandlerMapping, routing the requests to SockJsHttpRequestHandler
 * HTTP /websocket/ ~> webSocketHandlerMapping, routing the requests to WebSocketHttpRequestHandler
 * WS   /sockjs/    ~> the SockJS machinery that understands the payloads of the WS messages; and delegates to the
 *                     ``sockJsSocketHandler()``
 * WS   /websocket/ ~> the raw WebSocket machinery that does not really understand the payloads of the WS messages;
 *                     it only examines the URLs and delegates to ``websocketSocketHandler()``
 *
 * The messaging infrastructure is tied together by the underlying Spring Integration and Spring Messaging. The
 * important components are ``dispatchChannel()``, which is used on the receiving end of websockets: when a message
 * arrives, it goes on the ``dispatchChannel()`` channel. Both the SockJS and raw support then relies on the mapping
 * provided by ``messageAnnotationMessageHandler()``, which "attaches" the received messages to method in the
 * ``Controller``-annotated classes. (So, it works just like regular Spring MVC annotations. Slick!)
 *
 */
trait WebConfig {
  // require instances to be mixed in with CoreConfig
  this: CoreConfig =>
  val userQueueSuffixResolver = new SimpleUserQueueSuffixResolver()

  // Task executor for use in SockJS (heartbeat frames, correlationId timeouts)
  @Bean def taskScheduler(): TaskScheduler = {
    val taskScheduler = new ThreadPoolTaskScheduler()
    taskScheduler.setThreadNamePrefix("SockJS-")
    taskScheduler.setPoolSize(4)
    taskScheduler
  }

  // SockJS WS handler mapping
  @Bean def sockJsHandlerMapping(): SimpleUrlHandlerMapping = {
    val sockJsService = new DefaultSockJsService(taskScheduler())
    val requestHandler = new SockJsHttpRequestHandler(sockJsService, sockJsSocketHandler())

    val hm = new SimpleUrlHandlerMapping()
    hm.setOrder(-2)
    hm.setUrlMap(Collections.singletonMap("/sockjs/**", requestHandler))

    hm
  }

  // WebSocketHandler supporting STOMP messages
  @Bean def sockJsSocketHandler(): WebSocketHandler = {
    val stompHandler = new StompProtocolHandler()
    stompHandler.setUserQueueSuffixResolver(userQueueSuffixResolver)

    val webSocketHandler = new SubProtocolWebSocketHandler(dispatchChannel())
    webSocketHandler.setDefaultProtocolHandler(stompHandler)
    webSocketHandlerChannel().subscribe(webSocketHandler)

    webSocketHandler
  }

  @Bean def websocketSocketHandler(): WebSocketHandler = {
    val handler = new MessagingWebSocketHandler(dispatchChannel()) {
      override def afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        recogSessions().sessionEnded(RecogSessionId(session.getId))
      }
    }
    handler.setUriPrefix("/websocket/")
    handler
  }

  // MessageHandler for processing messages by delegating to @Controller annotated methods
  @Bean def messageAnnotationMessageHandler(): AnnotationMethodMessageHandler = {
    val handler = new AnnotationMethodMessageHandler(dispatchMessagingTemplate(), webSocketHandlerChannel())

    handler.setCustomArgumentResolvers(util.Arrays.asList(new SessionIdMehtodArgumentResolver))
    handler.setDestinationPrefixes(util.Arrays.asList("/app/"))
    handler.setMessageConverter(messageConverter)
    dispatchChannel().subscribe(handler)
    handler
  }

  // Raw WS handler mapping
  @Bean def webSocketHandlerMapping(): SimpleUrlHandlerMapping = {
    val requestHandler = new WebSocketHttpRequestHandler(websocketSocketHandler())

    val hm = new SimpleUrlHandlerMapping()
    hm.setOrder(-1)
    hm.setUrlMap(Collections.singletonMap("/websocket/**", requestHandler))

    hm
  }

  // MessageHandler that acts as a "simple" message broker
  // See DispatcherServletInitializer for enabling/disabling the "simple-broker" profile
  @Bean
  @Profile(Array("simple-broker"))
  def simpleBrokerMessageHandler(): SimpleBrokerMessageHandler = {
    val handler = new SimpleBrokerMessageHandler(webSocketHandlerChannel(), util.Arrays.asList("/topic/", "/queue/"))
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
  @Bean def userMessageHandler(): UserDestinationMessageHandler = {
    val handler = new UserDestinationMessageHandler(dispatchMessagingTemplate(), userQueueSuffixResolver)
    dispatchChannel().subscribe(handler)
    handler
  }

  // Channel for sending STOMP messages to connected WebSocket sessions (mostly for internal use)
  @Bean def webSocketHandlerChannel(): SubscribableChannel = new ExecutorSubscribableChannel(asyncExecutor())

}
