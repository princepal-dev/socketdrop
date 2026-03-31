package com.princeworks.socketdrop.response.room;

import com.princeworks.socketdrop.helper.Type;

public class ErrorResponse {
  private final Type type = Type.ERROR;
  private final String message;

  public ErrorResponse(String message) {
    this.message = message;
  }

  public Type getType() {
    return type;
  }

  public String getMessage() {
    return message;
  }
}

