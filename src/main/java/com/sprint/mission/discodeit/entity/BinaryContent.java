package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private final UUID id;
    private final Instant createdAt;
    private final String filename;
    private final String path;
    private final long size;
    private final String type;
    private final byte[] bytes;

    @Builder
    public BinaryContent(String filename, String path, long size, String type, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.filename = filename;
        this.path = path;
        this.size = size;
        this.type = type;
        this.bytes = bytes;
    }
}
