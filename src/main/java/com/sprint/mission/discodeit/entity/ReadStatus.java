package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity {
    private final UUID userId;
    private final UUID channelId;
    private Instant readTime;

    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.readTime = null;           // null을 넣는게 맞는가?
    }

    public void updateReadTime(Instant readTime) {
        this.readTime = readTime;
    }
}
