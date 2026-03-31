package com.princeworks.socketdrop.service.files.metadata;

import static org.junit.jupiter.api.Assertions.*;

import com.princeworks.socketdrop.model.file.FileMeta;
import org.junit.jupiter.api.Test;

class FileMetaDataRegistryImplTest {

  private final FileMetaDataRegistryImpl registry = new FileMetaDataRegistryImpl();

  @Test
  void addGetContainsAndRemoveWork() {
    FileMeta meta = new FileMeta("file_1", 10L, "a.txt", "room_1");

    registry.addRegistry("file_1", meta);

    assertTrue(registry.contains("file_1"));
    assertEquals(meta, registry.getDataFromRegistry("file_1"));
    assertEquals(meta, registry.removeDataFromRegistry("file_1"));
    assertFalse(registry.contains("file_1"));
  }
}

