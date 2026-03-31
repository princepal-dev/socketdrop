package com.princeworks.socketdrop.service.files.cleanup;

import static org.junit.jupiter.api.Assertions.*;

import com.princeworks.socketdrop.exception.ResourceNotFoundException;
import com.princeworks.socketdrop.model.file.FileMeta;
import com.princeworks.socketdrop.service.files.metadata.FileMetaDataRegistryImpl;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

class FileCleanupServiceImplTest {

  @TempDir Path tempDir;

  @Test
  void cleanupDeletesFileAndMetadata() throws Exception {
    FileMetaDataRegistryImpl registry = new FileMetaDataRegistryImpl();
    registry.addRegistry("file_1", new FileMeta("file_1", 4L, "x.txt", "room_1"));

    Path filePath = tempDir.resolve("file_1");
    Files.writeString(filePath, "test");

    FileCleanupServiceImpl service = new FileCleanupServiceImpl();
    ReflectionTestUtils.setField(service, "basePath", tempDir.toString());
    ReflectionTestUtils.setField(service, "fileMetaDataRegistry", registry);

    service.cleanupFile("file_1");

    assertFalse(Files.exists(filePath));
    assertFalse(registry.contains("file_1"));
  }

  @Test
  void cleanupThrowsWhenFileDoesNotExistAnywhere() {
    FileMetaDataRegistryImpl registry = new FileMetaDataRegistryImpl();

    FileCleanupServiceImpl service = new FileCleanupServiceImpl();
    ReflectionTestUtils.setField(service, "basePath", tempDir.toString());
    ReflectionTestUtils.setField(service, "fileMetaDataRegistry", registry);

    assertThrows(ResourceNotFoundException.class, () -> service.cleanupFile("missing"));
  }
}

