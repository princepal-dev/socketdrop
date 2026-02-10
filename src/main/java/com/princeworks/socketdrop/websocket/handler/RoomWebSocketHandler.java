package com.princeworks.socketdrop.websocket.handler;

import com.princeworks.socketdrop.websocket.session.SessionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class RoomWebSocketHandler extends TextWebSocketHandler {
  private static final Logger logger = LoggerFactory.getLogger(RoomWebSocketHandler.class);

  @Autowired private SessionRegistry sessionRegistry;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    String sessionId = session.getId();
    logger.info("WS CONNECTED : {}", sessionId);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
    String sessionId = session.getId();
    logger.info("WS DISCONNECTED : {}", sessionId);
    sessionRegistry.unregister(sessionId);
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) {
    String sessionId = session.getId();
    logger.error(
        "ERROR while connecting to : {}, error message :{}", sessionId, exception.getMessage());
    sessionRegistry.unregister(sessionId);
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) {
    logger.info("WS message from {} : {}", session.getId(), message.getPayload());
  }
}
