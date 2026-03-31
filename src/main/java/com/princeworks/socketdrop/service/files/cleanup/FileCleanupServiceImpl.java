package com.princeworks.socketdrop.service.files.cleanup;

import com.princeworks.socketdrop.exception.FileStorageException;
import com.princeworks.socketdrop.exception.InvalidArgumentException;
import com.princeworks.socketdrop.exception.ResourceNotFoundException;
import com.princeworks.socketdrop.helper.Operation;
import com.princeworks.socketdrop.service.files.metadata.FileMetaDataRegistry;
import com.princeworks.socketdrop.util.FileUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileCleanupServiceImpl implements FileCleanupService {
  @Value("${spring.file.base-path}")
  private String basePath;

  @Autowired private FileMetaDataRegistry fileMetaDataRegistry;

  @Override
  public void cleanupFile(String fileId) {
	if (fileId == null || fileId.trim().isEmpty()) {
	  throw new InvalidArgumentException("File id is required", "cleanupFile");
	}

	Path filePath = FileUtils.generatePath(basePath, fileId);
	boolean deletedFromRegistry = fileMetaDataRegistry.removeDataFromRegistry(fileId) != null;
	boolean fileExists = Files.exists(filePath);

	if (!deletedFromRegistry && !fileExists) {
	  throw new ResourceNotFoundException("File", "file id", fileId);
	}

	if (fileExists) {
	  try {
		Files.delete(filePath);
	  } catch (IOException e) {
		throw new FileStorageException(filePath.toString(), Operation.DELETE, e);
	  }
	}
  }
}
