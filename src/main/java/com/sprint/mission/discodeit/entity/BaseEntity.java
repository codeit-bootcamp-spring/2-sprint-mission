package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    protected final UUID id;
    protected final Instant createdAt;
    protected Instant updatedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }
}
