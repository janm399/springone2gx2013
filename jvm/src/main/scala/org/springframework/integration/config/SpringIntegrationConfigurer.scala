package org.springframework.integration.config

import scala.reflect.ClassTag

trait SpringIntegrationConfigurer {

  def gatewayProxy[A : ClassTag]: GatewayProxyFactoryBeanBuilder[A] = {
    GatewayProxyFactoryBeanBuilder[A](implicitly[ClassTag[A]])
  }

}
