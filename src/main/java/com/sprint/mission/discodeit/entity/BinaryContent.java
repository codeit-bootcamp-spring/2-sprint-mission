package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    public final Instant createdAt;

    private String fileName;
    private long size;
    private String contentType;
    private byte[] bytes;


    public BinaryContent(String fileName, long size, String contentType, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
