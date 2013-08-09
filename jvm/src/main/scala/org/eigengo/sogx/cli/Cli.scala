package org.eigengo.sogx.cli

import org.springframework.context.support.GenericXmlApplicationContext
import org.eigengo.sogx.core.RecogGateway

object Cli {

  def main(args: Array[String]): Unit = {
    val ctx = new GenericXmlApplicationContext(
      "classpath*:/META-INF/spring/integration/module-context.xml",
      "classpath*:/META-INF/spring/module-context.xml"
    )

    def run[U](f: => U): Unit = {
      println(">>")
      println(f)
      println("<<")
      println()
    }

    val recogService = ctx.getBean(classOf[RecogGateway])

    run(recogService.recogFrame("foo".getBytes))
    run(recogService.recogFrame("bar".getBytes))
    run(recogService.recogFrame("baz".getBytes))
  }
}
