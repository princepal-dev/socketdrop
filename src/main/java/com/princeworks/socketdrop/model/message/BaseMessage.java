package com.princeworks.socketdrop.model.message;

import com.princeworks.socketdrop.helper.Type;

public class BaseMessage{
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
