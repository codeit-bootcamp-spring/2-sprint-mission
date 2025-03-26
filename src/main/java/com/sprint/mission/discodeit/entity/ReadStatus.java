package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends SharedEntity{
    private final UUID userKey;
    private final UUID channelKey;
    private Instant lastReadAt;

    public ReadStatus(UUID userKey, UUID channelKey, Instant lastReadAt) {
        super();
        this.userKey = userKey;
        this.channelKey = channelKey;
        this.lastReadAt = lastReadAt;
    }

    public void updateLastReadAt(Instant lastReadAt) {
        this.lastReadAt = lastReadAt;
        setUpdatedAt(Instant.now());
    }
}
