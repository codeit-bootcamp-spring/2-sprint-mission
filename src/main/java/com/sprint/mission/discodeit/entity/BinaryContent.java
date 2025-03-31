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
    private final String contentType;
    private final Long size;
    private final byte[] bytes;

    public BinaryContent(String originalFileName, String contentType, long size, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.size = size;
        this.bytes = bytes;
    }
}