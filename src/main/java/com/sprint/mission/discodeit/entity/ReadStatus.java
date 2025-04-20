package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;

@Getter
public class ReadStatus extends BaseUpdatableEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private User user;
    private Channel channel;
    private Instant lastReadTime;

    public ReadStatus(User user, Channel channel) {
        this.user = user;
        this.channel = channel;
        this.lastReadTime = ZonedDateTime.now().toInstant();
    }

    public void updateLastReadTime() {
        this.lastReadTime = ZonedDateTime.now().toInstant();
    }
}
