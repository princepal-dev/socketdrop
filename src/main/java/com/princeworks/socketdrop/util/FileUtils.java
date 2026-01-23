package com.princeworks.socketdrop.util;

import java.nio.file.Path;

public final class FileUtils {
  private FileUtils() {}

  public static boolean sizeCheck(long actualSize, long maxAllowedSize) {
    return actualSize < maxAllowedSize;
  }

  public static Path generatePath(String baseDirectory, String fileId) {
    return Path.of(baseDirectory, fileId);
  }
}
