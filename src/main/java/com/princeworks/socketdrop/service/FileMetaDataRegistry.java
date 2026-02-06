package com.princeworks.socketdrop.service;

import com.princeworks.socketdrop.model.file.FileMeta;

public interface FileMetaDataRegistry {
  public FileMeta getDataFromRegistry(String fileId);
  public void addRegistry(String fileId, FileMeta metaData);
}
