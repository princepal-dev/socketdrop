package com.princeworks.socketdrop.model.file;

public class FileMeta {
    private String fileId;
    private Long fileSize;
    private String originalFileName;

    public FileMeta(String fileId, Long fileSize, String originalFileName) {
        this.fileId = fileId;
        this.fileSize = fileSize;
        this.originalFileName = originalFileName;
    }

    public String getFileId() {
        return fileId;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }
}
