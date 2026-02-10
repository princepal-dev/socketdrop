package com.princeworks.socketdrop.websocket.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.princeworks.socketdrop.model.message.BaseMessage;
import com.princeworks.socketdrop.model.message.JoinRoomMessage;
import com.princeworks.socketdrop.util.IdGenerator;
import com.princeworks.socketdrop.websocket.session.RoomRegistry;
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

  @Autowired private RoomRegistry roomRegistry;
  @Autowired private ObjectMapper objectMapper;
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
    try {
      String sessionId = session.getId();
      BaseMessage baseMessage = objectMapper.readValue(message.getPayload(), BaseMessage.class);

      if (baseMessage.getType() == null) {
        logger.info(
            "No TYPE is found in the session {} & payload {}", sessionId, message.getPayload());
        return;
      }

      switch (baseMessage.getType()) {
        case CREATE_ROOM:
          // Todo : Don't Allow the user to create room id's create it from the server and send it
          // back to the client
          break;
        case JOIN_ROOM:
          JoinRoomMessage joinRoomMessage =
              objectMapper.readValue(message.getPayload(), JoinRoomMessage.class);
          handleJoinRoom(session, joinRoomMessage);
          break;
        case LEAVE_ROOM:
          handleLeaveRoom(session);
          break;
        default:
          logger.info("Invalid TYPE provided!");
      }

    } catch (JsonProcessingException e) {
      logger.error("ERROR in Json processing : {}", e.getMessage());
      return;
    }
  }

  private void handleCreateRoom() {}

  private void handleJoinRoom(WebSocketSession session, JoinRoomMessage msg) {
    String roomId = msg.getRoomId();
    String sessionId = session.getId();
    String userName = IdGenerator.generateUsername();

    // Logging the info
    logger.info(
        "JOIN_ROOM from session : {}, username : {}, room id : {}", sessionId, userName, roomId);

    // Registering rooms & username to a particular session
    sessionRegistry.register(userName, sessionId);
    roomRegistry.joinRoom(sessionId, roomId);

    // Logging success
    logger.info("Room id : {} joined successfully!", roomId);
  }

  private void handleLeaveRoom(WebSocketSession session) {
    String sessionId = session.getId();

    // Leaving room & clearing session
    roomRegistry.leaveRoom(sessionId);
    sessionRegistry.unregister(sessionId);

    // Logging you have left room successfully
    logger.info("Session id : {} cleared successfully", sessionId);
  }
}
