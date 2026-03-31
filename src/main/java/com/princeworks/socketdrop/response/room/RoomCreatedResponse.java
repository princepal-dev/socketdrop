package com.princeworks.socketdrop.response.room;

import com.princeworks.socketdrop.helper.Type;

public class RoomCreatedResponse {
    private final Type type = Type.ROOM_CREATED;
    private final String roomId;
    private final String userId;
    private final String displayName;

    public RoomCreatedResponse(String roomId, String userId, String displayName) {
        this.roomId = roomId;
        this.userId = userId;
        this.displayName = displayName;
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

    public Type getType() {
        return type;
    }
}
