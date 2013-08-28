package org.eigengo.sogx.cli

import org.eigengo.sogx.config.CoreConfig
import java.util.concurrent.Executor
import org.springframework.core.task.SyncTaskExecutor
import org.springframework.context.annotation.{Bean, AnnotationConfigApplicationContext, ImportResource, Configuration}
import java.util.UUID
import org.eigengo.sogx._
import org.eigengo.sogx.core.{RecogService, RecogServiceActivator}
import org.springframework.integration.annotation.{Payload, Header}
import scala.annotation.tailrec
import java.io.{InputStream, BufferedInputStream, FileInputStream}

/**
 * Starts the command-line interface that shows that we can very trivially send the messages
 */
object Cli extends App {
  import Commands._
  import Utils.reader._

  /**
   * App is the Spring configuration-style class; it mixes in the ``CoreConfig`` trait and provides the
   * implementation of the remaining dependencies.
   *
   * It also instructs SF to load the integration components, which do not have convenient Java/Scala DSL [yet].
   */
  @Configuration
  @ImportResource(Array("classpath:/META-INF/spring/integration/module-context.xml"))
  class App extends CoreConfig {
    // we execute synchronously
    @Bean def asyncExecutor(): Executor = new SyncTaskExecutor
    // when we receive the response, we just print it
    @Bean def recogServiceActivator() = new RecogServiceActivator {
      def onCoinResponse(@Header correlationId: CorrelationId, @Payload coins: CoinResponse): Unit = println(">>> " + correlationId + ": " + coins)
    }
  }

  /**
   * Tail-recursive main command loop that waits for lines on standard input, then matches the input against
   * the known commands.
   *
   * When this function returns, the user has decided to quit the application.
   */
  @tailrec
  def commandLoop(): Unit = {
    Console.readLine() match {
      case QuitCommand                 => return

      case ImageCommand(fileName)      => readAll(fileName)(recogService.imageChunk(UUID.randomUUID().toString, _))
      case MJPEGCommand(fileName, fps) => readChunks(fileName, fps)(recogService.mjpegChunk(UUID.randomUUID().toString, _))

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

/**
 * Contains command regexes
 */
private[cli] object Commands {
  // this is a simple string
  val QuitCommand     = "quit"

  // regex matcher with the proper unapply method
  val ImageCommand    = "image:(.*)".r

  // custom unapply method
  object MJPEGCommand {
    private val regex = "mjpeg:([^?]*)(\\?fps=(\\d+))?".r

    def unapply(input: String): Option[(String, Int)] = {
      input match {
        case regex(fileName, _, fps) => Some((fileName, Option(fps).map(_.toInt).getOrElse(10)))
        case _                       => None
      }
    }
  }
}


/*

C521DAB7-391F-4AE1-86D7-878EBD6322E3/mjpeg:/coins1.mjpeg

*/