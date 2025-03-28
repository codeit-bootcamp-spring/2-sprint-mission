package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable, Identifiable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private final String filename;
    private final long size;
    private final String contentType;
    private final byte[] bytes;

    @Builder
    public BinaryContent(String filename, long size, String contentType, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.filename = filename;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
