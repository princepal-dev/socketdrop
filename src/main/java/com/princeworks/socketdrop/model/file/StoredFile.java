package com.princeworks.socketdrop.model.file;

import org.springframework.core.io.Resource;

public class StoredFile {
    private final Resource resource;
    private final FileMeta metaData;

    public StoredFile(FileMeta metaData, Resource resource) {
        this.metaData = metaData;
        this.resource = resource;
    }

    public FileMeta getMetaData() {
        return metaData;
    }

    public Resource getResource() {
        return resource;
    }
}
