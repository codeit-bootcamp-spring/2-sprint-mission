package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ReadStatus extends BaseEntity {
    private final UUID channelId;
    private final Map<UUID, Instant> userIds = new ConcurrentHashMap<>();

    public ReadStatus(UUID channelId) {
        this.channelId = channelId;
    }

    public void addUser(UUID userId) {
        userIds.put(userId, Instant.now());
    }

    public void removeUser(UUID userId) {
        userIds.remove(userId);
    }

    public void updateLastAccessTime(UUID userId) {
        userIds.put(userId, Instant.now());
    }
}
