package com.princeworks.socketdrop.exception;

public class InvalidMessageException extends RuntimeException {
  private final String reason;
  private final String clientMessage;

  public InvalidMessageException(String reason, String clientMessage) {
    super(clientMessage);
    this.reason = reason;
    this.clientMessage = clientMessage;
  }

  public String getClientMessage() {
    return clientMessage;
  }

  public String getReason() {
    return reason;
  }
}
