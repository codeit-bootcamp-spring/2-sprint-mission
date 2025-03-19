package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent {
    private final UUID id;
    private final Instant createdAt;

    private final BinaryContentType type;
    private final String originalFileName;

    private final long fileSize;

    public BinaryContent(BinaryContentType type, String originalFileName, long fileSize) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.type = type;
        this.originalFileName = originalFileName;
        this.fileSize = fileSize;
    }
}
