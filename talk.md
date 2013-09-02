#Initial commit
Start with the basic code. We need the main components: RecogService and RecogServiceActivator.

```scala
class RecogService {
  def mjpegChunk(id: CorrelationId)(data: ChunkData): Unit = ???
  def imageChunk(id: CorrelationId)(data: ChunkData): Unit = ???
}

trait RecogServiceActivator {
  def onCoinResponse(@Header correlationId: CorrelationId, @Payload coins: CoinResponse): Unit
}
```

Then we add the base type aliases (so that we can change our minds later and so that we don't mess our code with useless details).

```scala
// package.scala
package object sogx {
  type ImageData = Array[Byte]
  type ChunkData = Array[Byte]
  type CoinResponse = String 
}

// requests.scala
case class CorrelationId(value: String) extends AnyVal
```

> Initial commit

---

#RecogService and Activator
We add the ``RecogService`` and ``RecogServiceActivator``.

```scala
class RecogService(recogChannel: MessageChannel) {

  private def sendWithContentType(contentType: String, correlationId: CorrelationId, chunk: ChunkData): Unit = {
    val message = MessageBuilder.
      withPayload(chunk).
      setCorrelationId(correlationId).
      setHeader("content-type", contentType).
      build()

    recogChannel.send(message)
  }

  def imageChunk(correlationId: CorrelationId)(chunk: ChunkData) = 
  	sendWithContentType(ContentTypes.`image/*`, correlationId, chunk)

  def mjpegChunk(correlationId: CorrelationId)(chunk: ChunkData) = 
  	sendWithContentType(ContentTypes.`video/mjpeg`, correlationId, chunk)

}
```

```scala
trait RecogServiceActivator {
  def onCoinResponse(@Header correlationId: CorrelationId, coins: CoinResponse): Unit
}
```

> RecogService and Activator

---

#ChunkDecoder
We then wire in the chunk decoder, which completes our Scala code.

```scala
class ChunkDecoder(mjpegDecoder: MJPEGDecoder) {

  def decodeFrame(@Header correlationId: CorrelationId, @Header("content-type") contentType: String,
                  @Payload chunk: ChunkData): util.Collection[ImageData] = contentType match {
    case `video/mjpeg` => decodeMJPEGFrames(correlationId, chunk)
    case `image/*`     => decodeSingleImage(correlationId, chunk)
  }

  private def decodeSingleImage(correlationId: CorrelationId, chunk: ChunkData): util.Collection[ImageData] = 
  	Collections.singletonList(chunk)

  private def decodeMJPEGFrames(correlationId: CorrelationId, chunk: ChunkData): util.Collection[ImageData] = 
  	mjpegDecoder.decodeFrames(correlationId, chunk)


}
```

Now, on to the XML.

> ChunkDecoder

---

#Integration XML
Next up, we have the integration chain. The gold piece is the AMQP bit.

```
<int:chain input-channel="recogRequest">
    <int:service-activator id="activator" method="decodeFrame" ref="chunkDecoder"/>
    <int:splitter id="splitter" apply-sequence="false"/>
    <int-amqp:outbound-gateway exchange-name="sogx.exchange" routing-key="sogx.recog.key"
                               reply-timeout="250"
                               mapped-reply-headers="*" mapped-request-headers="*"
                               amqp-template="amqpTemplate"/>
    <int:object-to-string-transformer/>
    <int:service-activator ref="recogServiceActivator" method="onCoinResponse"/>
</int:chain>

<rabbit:connection-factory id="connectionFactory" host="localhost" channel-cache-size="10" />
<rabbit:template id="amqpTemplate" connection-factory="connectionFactory" />
```

> XML I

#Command-line application
Next up, we wire together the Spring application. We take advantage of Scala's traits and define the ``CoreConfig`` trait, which wires up the beans.

```scala
trait CoreConfig {
  @Bean def mjpegDecoder() = new MJPEGDecoder()
  @Bean def chunkDecoder() = new ChunkDecoder(mjpegDecoder())
  @Bean def recogService(): RecogService = new RecogService(recogRequest())


  @Bean def asyncExecutor(): Executor
  @Bean def recogServiceActivator(): RecogServiceActivator

  @Bean def recogRequest() = new DirectChannel()

}
```

And put together the ``Cli`` app.

```scala
object Cli extends App {
  import Commands._
  import Utils.reader._

  @Configuration
  @ImportResource(Array("classpath:/META-INF/spring/integration/module-context.xml"))
  class App extends CoreConfig {

    @Bean def asyncExecutor(): Executor = new SyncTaskExecutor

    @Bean def recogServiceActivator() = new RecogServiceActivator {
      def onCoinResponse(@Header correlationId: CorrelationId, @Payload coins: CoinResponse): Unit = println(">>> " + correlationId + ": " + coins)
    }
  }

  @tailrec
  def commandLoop(): Unit = {
    Console.readLine() match {
      case QuitCommand                 => return

      case ImageCommand(fileName)      => readAll(fileName)(recogService.imageChunk(UUID.randomUUID().toString))
      case MJPEGCommand(fileName, fps) => readChunks(fileName, fps)(recogService.mjpegChunk(UUID.randomUUID().toString))

      case null                        => // do nothing
      case _                           => println("wtf??")
    }

    // in tail position
    commandLoop()
  }

  // Create the Spring ApplicationContext implementation; register the @Configuration class and load it
  val ctx = new AnnotationConfigApplicationContext()
  ctx.register(classOf[App])
  ctx.refresh()

  // Grab the created ``RecogService`` implementation
  val recogService = ctx.getBean(classOf[RecogService])

  // start processing the user input
  commandLoop()

  // clean up
  ctx.close()
}
```

> Command-line application

---

#C++ code
Unfortunately, there are no responses. Why? Start RabbitMQ and write the C++ codebase.

```cpp
Main::Main(const std::string queue, const std::string exchange, const std::string routingKey) :
RabbitRpcServer::RabbitRpcServer(queue, exchange, routingKey) {
	
}

std::string Main::handleMessage(const AmqpClient::BasicMessage::ptr_t message, const AmqpClient::Channel::ptr_t channel) {
	Jzon::Object responseJson;
	try {
		// get the message, read the image
		ImageMessage imageMessage(message);
		auto imageData = imageMessage.headImage();
		auto imageMat = cv::imdecode(cv::Mat(imageData), 1);

		// ponies & unicorns
		Jzon::Array coinsJson;
		auto result = coinCounter.count(imageMat);
		for (auto i = result.coins.begin(); i != result.coins.end(); ++i) {
			Jzon::Object coinJson;
			Jzon::Object centerJson;
			centerJson.Add("x", i->center.x);
			centerJson.Add("y", i->center.y);
			coinJson.Add("center", centerJson);
			coinJson.Add("radius", i->radius);
			coinsJson.Add(coinJson);
		}
#ifdef WITH_RINGS
		responseJson.Add("hasRing", result.hasRing);
#endif		
		responseJson.Add("coins", coinsJson);
		responseJson.Add("succeeded", true);
	} catch (std::exception &e) {
		// bantha poodoo!
		std::cerr << e.what() << std::endl;
		responseJson.Add("succeeded", false);
	} catch (...) {
		// more bantha fodder!
		responseJson.Add("succeeded", false);
	}

	Jzon::Writer writer(responseJson, Jzon::NoFormat);
	writer.Write();

	return writer.GetResult();
}

int main(int argc, char** argv) {
	Main main("sogx.recog.queue", "sogx.exchange", "sogx.recog.key");
	main.runAndJoin(8);
	return 0;
}
```

> C++ code

---

#Laziness
For convenience, we can include RabbitMQ admin code to set up the RMQ.

```xml
<rabbit:admin id="rabbitAdmin" connection-factory="connectionFactory" auto-startup="true"/>

<rabbit:queue name="sogx.recog.queue" declared-by="rabbitAdmin"/>

<rabbit:direct-exchange name="sogx.exchange" declared-by="rabbitAdmin">
    <rabbit:bindings>
        <rabbit:binding queue="sogx.recog.queue" key="sogx.recog.key" />
    </rabbit:bindings>
</rabbit:direct-exchange>
```

> XML II

---

#Webapp structure
Next up, we define the webapp components. We have the ``RecogSessions`` and ``RecogController``.

```scala
class RecogSessions(messageSender: SimpMessageSendingOperations) {
  val sessions = new util.HashMap[RecogSessionId, CoinResponse]()

  def onCoinResponse(correlationId: CorrelationId, coins: CoinResponse): Unit = {
    sessions.put(RecogSessionId(correlationId.value), coins)
    sendSessions()
  }

  def sessionEnded(sessionId: RecogSessionId): Unit = {
    sessions.remove(sessionId)
    sendSessions()
  }

  private def sendSessions(): Unit = messageSender.convertAndSend("/topic/recog/sessions", sessions.values())

}
```

```scala
@Controller
class RecogController @Autowired()(recogService: RecogService, recogSessions: RecogSessions) {

  @MessageMapping(Array("/app/recog/image"))
  def image(@SessionId sessionId: RecogSessionId, @MessageBody body: ChunkData): Unit = {
    recogService.imageChunk(sessionId.value)(body)
  }

  @MessageMapping(Array("/app/recog/mjpeg"))
  def mjpeg(@SessionId sessionId: RecogSessionId, @MessageBody body: ChunkData): Unit = {
    recogService.mjpegChunk(sessionId.value)(body)
  }

  @RequestMapping(Array("/app/predef/image"))
  @ResponseBody
  def foo(): String = {
    val id = UUID.randomUUID().toString
    Utils.reader.readAll("/coins2.png")(recogService.imageChunk(id))
    recogSessions.sessionEnded(RecogSessionId(id))
    "image"
  }

  @RequestMapping(Array("/app/predef/coins"))
  @ResponseBody
  def bar(@RequestParam(defaultValue = "10") fps: Int): String = {
    val id = UUID.randomUUID().toString
    Utils.reader.readChunks("/coins2.mjpeg", fps)(recogService.mjpegChunk(id))
    recogSessions.sessionEnded(RecogSessionId(id))
    "coins"
  }

}
```

> Webapp structure

---

#Webapp core configuration
Unfortunately, the whole thing still doesn't work. We have the classes, but they are floating in nothingness. We need to wire in the configuration. First up, we'll be dealing with the recog sessions.

```scala
trait CoreConfig {
	
  @Bean def recogSessions() = new RecogSessions(dispatchMessagingTemplate())

  @Bean def messageConverter() = new DelegatingJsonMessageConverter(new MappingJackson2MessageConverter())

  @Bean def dispatchChannel() = new ExecutorSubscribableChannel(asyncExecutor())

  @Bean def dispatchMessagingTemplate(): SimpMessageSendingOperations = {
    val template = new SimpMessagingTemplate(dispatchChannel())
    template.setMessageConverter(messageConverter())
    template
  }

}
```

This brings together the two message buses. Now we can add the actual web mappings and the webapp.

> Webapp core configuration

---

#Webapp web configuration

```scala
trait WebConfig {
  // require instances to be mixed in with CoreConfig
  this: CoreConfig =>

  // Channel for sending STOMP messages to connected WebSocket sessions
  @Bean def webSocketHandlerChannel(): SubscribableChannel = new ExecutorSubscribableChannel(asyncExecutor())

  @Bean def taskScheduler(): TaskScheduler = {
    val taskScheduler = new ThreadPoolTaskScheduler()
    taskScheduler.setThreadNamePrefix("SockJS-")
    taskScheduler.setPoolSize(4)
    taskScheduler.afterPropertiesSet()

    taskScheduler
  }

  // MessageHandler that acts as a "simple" message broker
  // See DispatcherServletInitializer for enabling/disabling the "simple-broker" profile
  @Bean
  def simpleBrokerMessageHandler(): SimpleBrokerMessageHandler = {
    val handler = new SimpleBrokerMessageHandler(webSocketHandlerChannel(), util.Arrays.asList("/topic/", "/queue/"))
    dispatchChannel().subscribe(handler)
    handler
  }

  // WS -[SockJS]-> /sockjs/** ~> sockJsSocketHandler

  // SockJS WS handler mapping
  @Bean def sockJsHandlerMapping(): SimpleUrlHandlerMapping = {
    val handler = new SubProtocolWebSocketHandler(dispatchChannel())
    handler.setDefaultProtocolHandler(new StompProtocolHandler())
    webSocketHandlerChannel().subscribe(handler)

    val sockJsService = new DefaultSockJsService(taskScheduler())
    val requestHandler = new SockJsHttpRequestHandler(sockJsService, handler)

    val hm = new SimpleUrlHandlerMapping()
    hm.setOrder(-2)
    hm.setUrlMap(Collections.singletonMap("/sockjs/**", requestHandler))

    hm
  }

  // WS -[Raw]-> /websocket/** ~> websocketSocketHandler

  // Raw WS handler mapping
  @Bean def webSocketHandlerMapping(): SimpleUrlHandlerMapping = {
    val handler = new MessagingWebSocketHandler(dispatchChannel()) {
      override def afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        recogSessions().sessionEnded(RecogSessionId(session.getId))
      }
    }
    handler.setUriPrefix("/websocket/")

    val requestHandler = new WebSocketHttpRequestHandler(handler)

    val hm = new SimpleUrlHandlerMapping()
    hm.setOrder(-1)
    hm.setUrlMap(Collections.singletonMap("/websocket/**", requestHandler))

    hm
  }

  // MessageHandler for processing messages by delegating to @Controller annotated methods
  @Bean def annotationMethodMessageHandler(): AnnotationMethodMessageHandler = {
    val handler = new AnnotationMethodMessageHandler(dispatchMessagingTemplate(), webSocketHandlerChannel())

    handler.setCustomArgumentResolvers(util.Arrays.asList(new SessionIdMehtodArgumentResolver))
    handler.setDestinationPrefixes(util.Arrays.asList("/app/"))
    handler.setMessageConverter(messageConverter())
    dispatchChannel().subscribe(handler)
    handler
  }

}
```

> Webapp web configuration

#No running? DispatcherServlet
What's missing? Eh, Spring gurus?

```scala
@Configuration
@EnableWebMvc
@ComponentScan(basePackages=Array("org.eigengo.sogx"))
class Webapp extends WebMvcConfigurerAdapter with WebConfig with CoreConfig {

  @Bean def asyncExecutor() = {
    val executor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(4)
    executor.setCorePoolSize(8)
    executor.setThreadNamePrefix("MessageChannel-")
    executor
  }

  @Bean def recogServiceActivator() = new RecogServiceActivator {
    def onCoinResponse(@Header correlationId: CorrelationId, @Payload coins: CoinResponse): Unit =
      recogSessions().onCoinResponse(correlationId, coins)
  }

  // Allow serving HTML files through the default Servlet
  override def configureDefaultServletHandling(configurer: DefaultServletHandlerConfigurer) = {
    configurer.enable()
  }

}
```

and

```scala
class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  protected def getRootConfigClasses: Array[Class[_]] = {
    Array[Class[_]](classOf[Webapp])
  }

  protected def getServletConfigClasses: Array[Class[_]] = {
    Array[Class[_]](classOf[Webapp])
  }

  protected def getServletMappings: Array[String] = {
    Array[String]("/")
  }

  protected override def customizeRegistration(registration: ServletRegistration.Dynamic) {
    registration.setInitParameter("dispatchOptionsRequest", "true")
  }

}
```

> DispatcherServlet

---

#AngularJS application
We are going to wire in some UI. AngularJS!!

```html
<div ng-controller="SessionsCtrl">
    <tabs>
        <pane title="Raw">
            <h3>Raw data</h3>
            <pre><code>{{sessions}}</code></pre>
        </pane>
        <pane title="Canvas">
            <h3>Visual representation</h3>
            <div ng-repeat="coins in sessions">
                <canvas display="{{coins}}" fill="red" scale="0.4" dx="40" dy="80" width="500" height="386"></canvas>
            </div>
        </pane>
    </tabs>
</div>
```

> AngularJS application

---

#Mordor
Next up, let's spice things up with a little bit of CSS!

```html
<div id="mordor" ng-controller="SessionsCtrl">
    <div class="sessions">
        <div ng-class="{coins: true, glow: coins.hasRing}" ng-repeat="coins in sessions">
            <canvas display="{{coins}}" class="mordor" fill="#FDFDB8" scale="0.2"></canvas>
        </div>
    </div>
</div>
```

Show a demo of the eye. Rob, Guy et al.

> Mordor
