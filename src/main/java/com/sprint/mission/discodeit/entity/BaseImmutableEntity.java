package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BaseImmutableEntity {
    protected final UUID id;
    protected final Instant createdAt;

    public BaseImmutableEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }
}
