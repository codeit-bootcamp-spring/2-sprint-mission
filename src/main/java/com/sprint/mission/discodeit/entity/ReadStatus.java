package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus {
    private final UUID id;
    private final UUID userId;
    private final UUID channelId;
    private final Instant createdAt;
    private Instant updatedAt; // 마지막으로 메시지를 읽은 시간

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.createdAt = Instant.now();
    }

    public void updateReadStatus() {
        this.updatedAt = Instant.now();
    }
}
