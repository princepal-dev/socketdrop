package com.princeworks.socketdrop.model.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.princeworks.socketdrop.helper.Type;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseMessage{
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
