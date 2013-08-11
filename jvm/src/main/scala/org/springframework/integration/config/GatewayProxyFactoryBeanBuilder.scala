package org.springframework.integration.config

import java.util.concurrent.Executor
import scala.reflect.ClassTag
import org.springframework.integration.gateway.GatewayProxyFactoryBean

case class GatewayProxyFactoryBeanBuilder[A](serviceInterface: ClassTag[A], executor: Option[Executor] = None) {

  def withAsyncExecutor(executor: Executor): GatewayProxyFactoryBeanBuilder[A] = {
    copy(executor = Some(executor))
  }

  private def build(): GatewayProxyFactoryBean = {
    val gatewayProxyFactoryBean = new GatewayProxyFactoryBean()
    gatewayProxyFactoryBean.setServiceInterface(serviceInterface.runtimeClass)
    executor.map(gatewayProxyFactoryBean.setAsyncExecutor)

    gatewayProxyFactoryBean
  }
}

object GatewayProxyFactoryBeanBuilder {

  implicit def toGatewayProxyFactoryBean(builder: GatewayProxyFactoryBeanBuilder[_]) = {
    builder.build()
  }

}
