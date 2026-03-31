package com.princeworks.socketdrop.service.files.storage;

import static org.junit.jupiter.api.Assertions.*;

import com.princeworks.socketdrop.model.file.StoredFile;
import com.princeworks.socketdrop.response.file.UploadResponse;
import com.princeworks.socketdrop.service.files.cleanup.FileCleanupServiceImpl;
import com.princeworks.socketdrop.service.files.metadata.FileMetaDataRegistryImpl;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

class FileStorageServiceImplTest {

  @TempDir Path tempDir;

  @Test
  void uploadDownloadAndDeleteFlowWorks() throws Exception {
    FileMetaDataRegistryImpl registry = new FileMetaDataRegistryImpl();
    FileCleanupServiceImpl cleanupService = new FileCleanupServiceImpl();
    ReflectionTestUtils.setField(cleanupService, "basePath", tempDir.toString());
    ReflectionTestUtils.setField(cleanupService, "fileMetaDataRegistry", registry);

    FileStorageServiceImpl storageService = new FileStorageServiceImpl();
    ReflectionTestUtils.setField(storageService, "allowedSize", 1024L * 1024L);
    ReflectionTestUtils.setField(storageService, "basePath", tempDir.toString());
    ReflectionTestUtils.setField(storageService, "fileMetaDataRegistry", registry);
    ReflectionTestUtils.setField(storageService, "fileCleanupService", cleanupService);

    MockMultipartFile file =
        new MockMultipartFile("file", "hello.txt", "text/plain", "hello".getBytes());

    UploadResponse uploadResponse = storageService.uploadFile(file, "room_1");
    assertNotNull(uploadResponse.getFileId());

    StoredFile storedFile = storageService.downloadFile(uploadResponse.getFileId());
    assertEquals("hello.txt", storedFile.getMetaData().getOriginalFileName());
    assertEquals("room_1", storedFile.getMetaData().getRoomId());

    storageService.deleteFile(uploadResponse.getFileId());

    assertFalse(Files.exists(tempDir.resolve(uploadResponse.getFileId())));
    assertFalse(registry.contains(uploadResponse.getFileId()));
  }
}

