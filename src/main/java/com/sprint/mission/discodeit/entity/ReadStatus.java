package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseUpdatableEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadTime;

    public ReadStatus(UUID userId, UUID channelId) {
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadTime = ZonedDateTime.now().toInstant();
    }

    public void updateLastReadTime() {
        this.lastReadTime = ZonedDateTime.now().toInstant();
    }
}
