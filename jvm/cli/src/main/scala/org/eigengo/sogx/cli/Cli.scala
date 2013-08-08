package org.eigengo.sogx.cli

import org.springframework.context.support.GenericXmlApplicationContext
import org.eigengo.sogx.core.RecogService

object Cli extends App {

  val ctx = new GenericXmlApplicationContext(
    "classpath*:/META-INF/spring/integration/module-context.xml",
    "classpath*:/META-INF/spring/module-context.xml"
  )

  val recogService = ctx.getBean(classOf[RecogService])

  recogService.recogFrame("foo".getBytes)
  recogService.recogFrame("bar".getBytes)
  recogService.recogFrame("baz".getBytes)

}
