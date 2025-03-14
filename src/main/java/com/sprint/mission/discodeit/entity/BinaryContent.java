package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent {
    private final UUID binaryContentId;
    public final Instant createdAt;

    public BinaryContent() {
        binaryContentId = UUID.randomUUID();
        createdAt = Instant.now();
    }
}
