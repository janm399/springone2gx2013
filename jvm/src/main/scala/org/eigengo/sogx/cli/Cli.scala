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

  val ctx = new AnnotationConfigApplicationContext()
  ctx.register(classOf[App])
  ctx.refresh()

  val recogService = ctx.getBean(classOf[RecogService])

  @tailrec
  def commandLoop(): Unit = {
    Console.readLine() match {
      case QuitCommand                => return

      case ImageCommand(id, fileName) => readAll(fileName)(recogService.imageChunk(id, _))
      case H264Command(id, fileName)  => readChunks(fileName, 64)(recogService.h264Chunk(id, _))
      case MJPEGCommand(id, fileName) => readChunks(fileName, 64)(recogService.mjpegChunk(id, _))

      case _                          => println("wtf??")
    }

    commandLoop()
  }

  commandLoop()
}

object Commands {
  private def uuidAnd(rest: String) = s"([0-9a-z\\-]{36})/$rest".r

  val ImageCommand    = uuidAnd("image:(.*)")
  val H264Command     = uuidAnd("h264:(.*)")
  val MJPEGCommand    = uuidAnd("mjpeg:(.*)")
  val QuitCommand     = "quit"

}
