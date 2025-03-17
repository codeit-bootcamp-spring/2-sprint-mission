package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public final class BinaryContent {
    private final UUID id;
    private final Instant createTime;
    private final UUID referenceId;
    private final String fileName;
    private final String fileType;
    private final String filePath;

    public BinaryContent(UUID referenceId, String fileName, String fileType, String filePath) {
        this.id = UUID.randomUUID();
        this.createTime = Instant.now();
        this.referenceId = referenceId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "BinaryContent{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", referenceId=" + referenceId +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
