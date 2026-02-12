package com.princeworks.socketdrop.model.user;

public class UserSessionInfo {
  private final String userId;
  private final String displayName;

  public UserSessionInfo(String userId, String displayName) {
    this.displayName = displayName;
    this.userId = userId;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getUserId() {
    return userId;
  }
}
