package com.princeworks.socketdrop.controller;

import com.princeworks.socketdrop.exception.ResourceNotFoundException;
import com.princeworks.socketdrop.model.file.FileMeta;
import com.princeworks.socketdrop.model.file.StoredFile;
import com.princeworks.socketdrop.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file/downloads")
public class FileDownloadController {
  @Autowired private FileStorageService fileStorageService;

  @GetMapping("/{fileId}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
    StoredFile fileFromServer = fileStorageService.downloadFile(fileId);
    FileMeta metadata = fileFromServer.getMetaData();

    if (metadata == null)
      throw new ResourceNotFoundException("File", "file id", fileId);

    Resource resource = fileFromServer.getResource();
    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + metadata.getOriginalFileName() + "\"")
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .contentLength(metadata.getFileSize())
        .body(resource);
  }
}
