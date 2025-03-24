package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class BinaryContent {
    private final UUID id;
    private final UUID userId;
    private final UUID messageId;
    private final Instant createdAt;

    public BinaryContent(UUID userId, UUID messageId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.messageId = messageId;
        this.createdAt = Instant.now();
    }
}
