package com.princeworks.socketdrop.websocket.session;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// It answers only these questions:
// Who is connected?
// Which user belongs to which session?
// Which session belongs to which user ?

public class SessionRegistry {
  ConcurrentMap<String, String> userToSession = new ConcurrentHashMap<>();
  ConcurrentMap<String, String> sessionToUser = new ConcurrentHashMap<>();

  public void register(String userName, String sessionId) {
    sessionToUser.put(sessionId, userName);
    userToSession.put(userName, sessionId);
  }

  public void unregister(String sessionId) {
    String user = sessionToUser.remove(sessionId);
    if (user != null) {
      userToSession.remove(user);
    }
  }

  public String getUser(String sessionId) {
    return sessionToUser.get(sessionId);
  }

  public String getSessionId(String userName) {
    return userToSession.get(userName);
  }
}
