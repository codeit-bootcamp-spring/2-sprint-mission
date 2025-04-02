package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BinaryContent extends BaseEntity {
    private final String filePath;
    private final String fileName;
    private final String fileType;
    private final long fileSize;

    public BinaryContent(String filePath, String fileName, String fileType, long fileSize) {
        super();
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public BinaryContent(UUID id, String filePath, String fileName, String fileType, long fileSize) {
        super(id);
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }
}
