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

    private final BinaryContentType type;
    private final String originalFileName;
    private final String filePath;
    private final long fileSize;
    private final String extension;

    public BinaryContent(BinaryContentType type, String originalFileName, String filePath, long fileSize,
                         String extension) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.type = type;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.extension = extension;
    }
}
