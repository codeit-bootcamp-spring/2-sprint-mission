package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BaseEntity {
    protected final UUID id;
    protected final Long createdAt;
    protected Long updatedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();  // UUID 자동 생성
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = this.createdAt;
    }

    public void updateTimestamp() {
        this.updatedAt = Instant.now().getEpochSecond();
    }
}
