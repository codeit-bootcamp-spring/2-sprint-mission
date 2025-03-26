package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;

    private final String originalFileName;
    private final String type;
    private final Long fileSize;
    private final String filePath;

    public BinaryContent(String originalFileName, String type, long fileSize, String filePath) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.originalFileName = originalFileName;
        this.type = type;
        this.fileSize = fileSize;
        this.filePath = filePath;
    }
}