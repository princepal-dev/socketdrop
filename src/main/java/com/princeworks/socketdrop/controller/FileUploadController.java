package com.princeworks.socketdrop.controller;

import com.princeworks.socketdrop.exception.InvalidArgumentException;
import com.princeworks.socketdrop.security.response.UploadResponse;
import com.princeworks.socketdrop.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file/uploads")
public class FileUploadController {
  @Autowired private FileStorageService fileStorageService;

  @PostMapping
  public ResponseEntity<UploadResponse> handleUploads(@RequestParam("file") MultipartFile file) {
    if (file == null || file.isEmpty())
          throw new InvalidArgumentException("Error : File is required!", "file upload");

    UploadResponse response = fileStorageService.uploadFile(file);
    return ResponseEntity.ok(response);
  }
}
