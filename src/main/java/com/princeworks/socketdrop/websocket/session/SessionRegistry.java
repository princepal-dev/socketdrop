package com.princeworks.socketdrop.websocket.session;

import com.princeworks.socketdrop.model.user.UserSessionInfo;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// It answers only these questions:
// Who is connected?
// Which user belongs to which session?
// Which session belongs to which user ?

@Component
public class SessionRegistry {
  ConcurrentMap<String, UserSessionInfo> sessionToUser = new ConcurrentHashMap<>();

  public void register(String sessionId, UserSessionInfo userInfo) {
    sessionToUser.put(sessionId, userInfo);
  }

  public void unregister(String sessionId) {
    sessionToUser.remove(sessionId);
  }

  public UserSessionInfo getUserName(String sessionId) {
    return sessionToUser.get(sessionId);
  }

  public boolean isRegistered(String sessionId) {
    return sessionToUser.containsKey(sessionId);
  }
}
