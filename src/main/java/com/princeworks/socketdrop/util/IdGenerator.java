package com.princeworks.socketdrop.util;

import java.util.UUID;

public final class IdGenerator {
  private IdGenerator() {}

  public static String generateRandomId() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

  public static String generateFileId() {
    return "file_" + generateRandomId();
  }

  public static String generateRoomId() {
    return "room_" + generateRandomId();
  }
}
