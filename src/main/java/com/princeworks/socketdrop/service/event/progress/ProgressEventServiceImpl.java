package com.princeworks.socketdrop.service.event.progress;

import com.princeworks.socketdrop.response.file.UploadProgressResponse;
import com.princeworks.socketdrop.websocket.messging.WebSocketMessagingService;
import com.princeworks.socketdrop.websocket.session.RoomRegistry;
import com.princeworks.socketdrop.websocket.session.SessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class ProgressEventServiceImpl implements ProgressEventService {
  @Autowired private RoomRegistry roomRegistry;
  @Autowired private SessionRegistry sessionRegistry;
  @Autowired private WebSocketMessagingService webSocketMessagingService;

  @Override
  public void notifyUploadStarted(String roomId, String fileName, long fileSize) {
	sendToRoom(
		roomId, new UploadProgressResponse(roomId, "STARTED", null, fileName, fileSize, "Upload started"));
  }

  @Override
  public void notifyUploadCompleted(String roomId, String fileId, String fileName, long fileSize) {
	sendToRoom(
		roomId,
		new UploadProgressResponse(
			roomId, "COMPLETED", fileId, fileName, fileSize, "Upload completed"));
  }

  @Override
  public void notifyUploadFailed(String roomId, String fileName, String reason) {
	sendToRoom(
		roomId,
		new UploadProgressResponse(roomId, "FAILED", null, fileName, null, reason));
  }

  private void sendToRoom(String roomId, UploadProgressResponse payload) {
	if (roomId == null || roomId.trim().isEmpty() || !roomRegistry.roomExists(roomId)) {
	  return;
	}

	for (String sessionId : roomRegistry.getSessions(roomId)) {
	  WebSocketSession session = sessionRegistry.getSocket(sessionId);
	  if (session != null) {
		webSocketMessagingService.sendToSession(session, payload);
	  }
	}
  }
}
