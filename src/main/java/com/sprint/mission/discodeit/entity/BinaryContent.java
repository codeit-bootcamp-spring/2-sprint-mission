package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final String fileName;
    private final String contentType;
    private final Instant createdAt;
    private final byte[] bytes;

    public BinaryContent(String filename, String contentType, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = ZonedDateTime.now().toInstant();
        this.fileName = filename;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
