package com.princeworks.socketdrop.service.files.storage;

import com.princeworks.socketdrop.model.file.StoredFile;
import com.princeworks.socketdrop.response.file.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    UploadResponse uploadFile(MultipartFile file, String roomId);
    StoredFile downloadFile(String fileId);
    void deleteFile(String fileId);
}
