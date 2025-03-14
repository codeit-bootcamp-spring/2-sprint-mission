package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent {
    private final UUID id;
    private final Instant createdAt;
    private final byte[] binaryImage;

    public BinaryContent(byte[] binaryImage) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.binaryImage = binaryImage;
    }
}
