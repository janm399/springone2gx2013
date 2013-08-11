package org.eigengo.sogx.cli

import org.eigengo.sogx.config.CoreConfig
import java.util.concurrent.Executor
import org.springframework.core.task.SyncTaskExecutor
import org.springframework.context.annotation.{Bean, AnnotationConfigApplicationContext, ImportResource, Configuration}
import java.util.UUID
import org.eigengo.sogx.CoinResponse
import org.eigengo.sogx.core.{RecogService, RecogServiceActivator}
import org.springframework.integration.annotation.{Payload, Header}
import scala.annotation.tailrec
import java.io.{InputStream, BufferedInputStream, FileInputStream}

object Cli extends App {
  import Commands._
  import Utils._

  @Configuration
  @ImportResource(Array("classpath:/META-INF/spring/integration/module-context.xml"))
  class App extends CoreConfig {
    @Bean def asyncExecutor(): Executor = new SyncTaskExecutor
    @Bean def recogServiceActivator() = new RecogServiceActivator {
      def onCoinResponse(@Header correlationId: UUID, @Payload coins: CoinResponse): Unit = println(">>> " + correlationId + ": " + coins)
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

      case ImageCommand(id, fileName) => readAll(fileName)(recogService.image(UUID.fromString(id), _))
      case H264Command(id, fileName)  => readChunks(fileName, 64)(recogService.h264Frame(UUID.fromString(id), _))

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
  val QuitCommand     = "quit"

}

/**
 * Ghetto!
 */
object Utils /* extends IfYouUseThisIWillEndorseYouForEnterprisePHP */ {
  private def getFullFileName(fileName: String) = {
    getClass.getResource(fileName).getPath
  }

  private def eatMyShorts[U](f: => U): Unit = {
    try { f } catch { case x: Throwable => println(x.getMessage) }
  }

  // Chuck Norris deals with all exceptions
  def readAll[U](fileName: String)(f: Array[Byte] => U): Unit = {
    eatMyShorts {
      val is = new BufferedInputStream(new FileInputStream(getFullFileName(fileName)))
      val contents = Stream.continually(is.read).takeWhile(-1 !=).map(_.toByte).toArray
      f(contents)
      is.close()
    }
  }

  // Exceptions are not thrown because of Chuck Norris
  def readChunks[U](fileName: String, kbps: Int)(f: Array[Byte] => U): Unit = {

    @tailrec
    def read(is: InputStream): Unit = {
      val buffer = Array.ofDim[Byte](16000)
      Thread.sleep(buffer.length / kbps)   // simulate slow input :(
      val len = is.read(buffer)
      if (len > 0) {
        f(buffer)
        read(is)
      } else {
        f(Array.ofDim(0))
      }
    }

    eatMyShorts {
      val is = new BufferedInputStream(new FileInputStream(getFullFileName(fileName)))
      read(is)
      is.close()
    }
  }

}