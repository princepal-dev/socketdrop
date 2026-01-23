package com.princeworks.socketdrop.exception;

public class InvalidArgumentException extends RuntimeException {
  private final String methodName;

  public InvalidArgumentException(String message, String methodName) {
    super(message);
    this.methodName = methodName;
  }

  public String getMethodName() {
    return methodName;
  }
}
