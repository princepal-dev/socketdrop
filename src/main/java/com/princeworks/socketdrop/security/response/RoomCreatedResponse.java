package com.princeworks.socketdrop.security.response;

import com.princeworks.socketdrop.helper.Type;

public class RoomCreatedResponse {
    private final Type type = Type.ROOM_CREATED;
    private final String roomId;

    public RoomCreatedResponse(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public Type getType() {
        return type;
    }
}
