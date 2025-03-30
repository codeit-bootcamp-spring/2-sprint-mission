package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID channelId;
    private final UUID userId;
    private Instant readTime;
    //private final Map<UUID, Instant> userIds = new ConcurrentHashMap<>();

    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.readTime = Instant.now();
    }

    public void updateLastAccessTime() {
        this.readTime = Instant.now();
    }

    @Override
    public String toString() {
        return "ReadStatus{" +
                "id=" + getId() +
                ", channelId=" + channelId +
                ", userIds=" + userId +
                ", readTime=" + readTime +
                '}';
    }
}
