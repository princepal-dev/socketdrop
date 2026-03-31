package com.princeworks.socketdrop.response.room;

import com.princeworks.socketdrop.helper.Type;

public class RoomJoinedResponse {
  private final Type type = Type.ROOM_JOINED;
  private final String roomId;
  private final String userId;
  private final String displayName;

  public RoomJoinedResponse(String roomId, String userId, String displayName) {
    this.roomId = roomId;
    this.userId = userId;
    this.displayName = displayName;
  }

  public Type getType() {
    return type;
  }

  public String getRoomId() {
    return roomId;
  }

  public String getUserId() {
    return userId;
  }

  public String getDisplayName() {
    return displayName;
  }
}

