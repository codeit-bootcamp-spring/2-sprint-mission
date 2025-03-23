package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent {
    private UUID id;
    private Instant createdAt;
    private String filePath;

    public BinaryContent(String filePath) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.filePath = filePath;
    }
}
