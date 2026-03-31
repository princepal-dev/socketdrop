package com.princeworks.socketdrop.service.files.metadata;

import com.princeworks.socketdrop.model.file.FileMeta;

public interface FileMetaDataRegistry {
  FileMeta getDataFromRegistry(String fileId);

  void addRegistry(String fileId, FileMeta metaData);

  FileMeta removeDataFromRegistry(String fileId);

  boolean contains(String fileId);
}
