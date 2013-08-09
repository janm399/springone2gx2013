package org.eigengo.sogx.config

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer
import javax.servlet.{ServletContext, ServletRegistration}

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

  override def onStartup(servletContext: ServletContext) {
    servletContext.setInitParameter("spring.profiles.active", "simple-broker")
    super.onStartup(servletContext)
  }
}
