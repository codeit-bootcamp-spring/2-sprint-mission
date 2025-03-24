package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = ZonedDateTime.now().toInstant();
    private Instant updatedAt = null;

    public void updateTime() {
        this.updatedAt = ZonedDateTime.now().toInstant();
    }
}
