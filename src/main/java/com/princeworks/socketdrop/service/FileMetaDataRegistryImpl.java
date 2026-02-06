package com.princeworks.socketdrop.service;

import com.princeworks.socketdrop.model.file.FileMeta;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class FileMetaDataRegistryImpl implements FileMetaDataRegistry {
    ConcurrentMap<String, FileMeta> fileRegistry = new ConcurrentHashMap<>();

    @Override
    public void addRegistry(String fileId, FileMeta metaData) {
        fileRegistry.put(fileId, metaData);
    }

    @Override
    public FileMeta getDataFromRegistry(String fileId) {
        return fileRegistry.get(fileId);
    }
}
