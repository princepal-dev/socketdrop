package com.princeworks.socketdrop.service;

import com.princeworks.socketdrop.model.file.StoredFile;
import com.princeworks.socketdrop.security.response.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    UploadResponse uploadFile(MultipartFile file);
    StoredFile downloadFile(String fileId);
}
