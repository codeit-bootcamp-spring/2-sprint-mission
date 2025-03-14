package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent {
    private final UUID id;
    private final UUID userId;
    private final UUID messageId;
    private final Instant createdAt; // final 여부? 변할 필요 없으니깐?

    public BinaryContent(UUID id, UUID userId, UUID messageId) {
        this.id = id;
        this.userId = userId;
        this.messageId = messageId;
        this.createdAt = Instant.now();
    }
}
