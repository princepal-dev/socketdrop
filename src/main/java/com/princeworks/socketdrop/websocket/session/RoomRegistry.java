package com.princeworks.socketdrop.websocket.session;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoomRegistry {
  ConcurrentMap<String, String> sessionToRoom = new ConcurrentHashMap<>();
  ConcurrentMap<String, Set<String>> roomToSessionId = new ConcurrentHashMap<>();

  public void joinRoom(String sessionId, String roomId) {
    // 1. Remove from old room (if exists)
    String oldRoom = sessionToRoom.get(sessionId);
    if (oldRoom != null) {
      Set<String> oldRoomSessions = roomToSessionId.get(oldRoom);
      if (oldRoomSessions != null) {
        oldRoomSessions.remove(sessionId);

        // cleanup empty room
        if (oldRoomSessions.isEmpty()) {
          roomToSessionId.remove(oldRoom);
        }
      }
    }

    // 2. Add to new room
    roomToSessionId.computeIfAbsent(roomId, r -> ConcurrentHashMap.newKeySet()).add(sessionId);

    // 3. Update session → room mapping
    sessionToRoom.put(sessionId, roomId);
  }

  public void leaveRoom(String sessionId) {
    String room = sessionToRoom.remove(sessionId);
    if (room == null) return;

    Set<String> sessions = roomToSessionId.get(room);
    if (sessions != null) {
      sessions.remove(sessionId);

      // cleanup empty room
      if (sessions.isEmpty()) {
        roomToSessionId.remove(room);
      }
    }
  }

  public String getRoom(String sessionId) {
    return sessionToRoom.get(sessionId);
  }

  public Set<String> getSessions(String roomId) {
    Set<String> sessions = roomToSessionId.get(roomId);
    return sessions == null ? Collections.emptySet() : Collections.unmodifiableSet(sessions);
  }
}
