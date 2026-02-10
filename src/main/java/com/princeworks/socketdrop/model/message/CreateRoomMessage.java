package com.princeworks.socketdrop.model.message;

public class CreateRoomMessage extends BaseMessage {
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
