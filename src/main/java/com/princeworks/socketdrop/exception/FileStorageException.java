package com.princeworks.socketdrop.exception;

import com.princeworks.socketdrop.util.Operation;

public class FileStorageException extends RuntimeException {
  public FileStorageException(String target, Operation operation, Throwable cause) {
    super(String.format("%s failed on %s", operation, target), cause);
  }

  public FileStorageException(String target, Operation operation, String message) {
    super(String.format("%s failed on %s : %s", operation, target, message));
  }
}
