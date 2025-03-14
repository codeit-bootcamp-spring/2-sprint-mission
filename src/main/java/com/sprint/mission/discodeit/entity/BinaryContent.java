package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent {
    private final UUID id;
    private final Instant createdAt;
    private final String filename;
    private final byte[] content;


    public BinaryContent(UUID id, Instant createdAt, String filename, byte[] content) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.filename = filename;
        this.content = content;
    }
}
