package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BaseEntity implements Serializable {

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public BaseEntity() {
        id = UUID.randomUUID();
        createdAt = Instant.now().getEpochSecond();
        updatedAt = createdAt;
    }

    public void updateUpdatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
    }
}
