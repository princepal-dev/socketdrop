package com.princeworks.socketdrop.model.file;

public class FileMeta {
    private String fileId;
    private Long fileSize;
    private String originalFileName;
    private String roomId;

    public FileMeta(String fileId, Long fileSize, String originalFileName, String roomId) {
        this.fileId = fileId;
        this.fileSize = fileSize;
        this.originalFileName = originalFileName;
        this.roomId = roomId;
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

    public String getRoomId() {
        return roomId;
    }
}
