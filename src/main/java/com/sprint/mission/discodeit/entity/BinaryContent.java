package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private String fileName;
    private Long size;
    private String contentType;
    private final byte[] binaryImage;

    public BinaryContent(String fileName, Long size, String contentType, byte[] binaryImage) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.binaryImage = binaryImage;
    }
}
