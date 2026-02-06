package com.princeworks.socketdrop.service;

import com.princeworks.socketdrop.exception.FileStorageException;
import com.princeworks.socketdrop.exception.InvalidArgumentException;
import com.princeworks.socketdrop.exception.ResourceNotFoundException;
import com.princeworks.socketdrop.model.file.FileMeta;
import com.princeworks.socketdrop.model.file.StoredFile;
import com.princeworks.socketdrop.security.response.UploadResponse;
import com.princeworks.socketdrop.util.FileUtils;
import com.princeworks.socketdrop.util.IdGenerator;
import com.princeworks.socketdrop.util.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileStorageServiceImpl implements FileStorageService {
  @Value("${spring.file.max-size}")
  private long allowedSize;

  @Value("${spring.file.base-path}")
  private String basePath;

  @Autowired private FileMetaDataRegistry fileMetaDataRegistry;

  @Override
  public UploadResponse uploadFile(MultipartFile file) {
    if (!FileUtils.sizeCheck(file.getSize(), allowedSize)) {
      throw new InvalidArgumentException("You have exceeded the upload size limit", "uploadFile");
    }

    String fileId = IdGenerator.generateFileId();
    String originalFileName = file.getOriginalFilename();

    try {
      // ensure base path exists
      Path baseDir = Path.of(basePath);
      Files.createDirectories(baseDir);

      // resolve file path
      Path filePath = baseDir.resolve(fileId);

      // transfer the file
      file.transferTo(filePath);
    } catch (IOException e) {
      throw new FileStorageException(basePath, Operation.UPLOAD, e);
    }

    fileMetaDataRegistry.addRegistry(
        fileId, new FileMeta(fileId, file.getSize(), originalFileName));

    UploadResponse response = new UploadResponse();
    response.setFileId(fileId);
    response.setFileName(originalFileName);
    response.setFileSize(file.getSize());

    return response;
  }

  @Override
  public StoredFile downloadFile(String fileId) {
    Path filePath = Path.of(basePath).resolve(fileId);

    if (!Files.exists(filePath)) {
      throw new ResourceNotFoundException("File", "file id", fileId);
    }

    if (!Files.isReadable(filePath)) {
      throw new FileStorageException(
          filePath.toString(), Operation.READ, new IOException("File is not readable"));
    }

    try {
      FileMeta metadata = fileMetaDataRegistry.getDataFromRegistry(fileId);
      return new StoredFile(
              metadata, new InputStreamResource(Files.newInputStream(filePath)));
    } catch (IOException e) {
      throw new FileStorageException(filePath.toString(), Operation.READ, e);
    }
  }
}
