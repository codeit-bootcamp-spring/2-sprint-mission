package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReadStatus {
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;  // 유저가 해당 채널에 들어갈 때마다 수정(마지막으로 읽은 시간)

    private final UUID userId;
    private final UUID channelId;

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.userId = userId;
        this.channelId = channelId;
    }
}
