package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ReadStatus extends BaseEntity {
    private final UUID userId;
    private final Map<UUID, Instant> channelIds = new ConcurrentHashMap<>();

    public ReadStatus(UUID userId) {
        this.userId = userId;
    }

    public void addChannel(UUID channelId) {
        channelIds.put(channelId, Instant.now());
    }

    public void removeChannel(UUID channelId) {
        channelIds.remove(channelId);
    }

    public void updateLastAccessTime(UUID channelId) {
        channelIds.put(channelId, Instant.now());
    }
}
