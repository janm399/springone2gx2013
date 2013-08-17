package org.springframework.web.socket.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.*;

import java.util.Collection;
import java.util.Collections;

/**
 * Convenience adapter that channels WS messages to methods in <code>@Controller</code>-annoated
 * beans.
 */
public class AnnotationWebSocketHandlerAdapter implements WebSocketHandler, ApplicationContextAware {
    private Collection<Object> handlers = Collections.emptyList();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.handlers = applicationContext.getBeansWithAnnotation(Controller.class).values();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("Msg " + message + " to " + handlers);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
