package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public abstract class Entity {
    protected final UUID id;
    protected final long createdAt;
    protected long updatedAt;

    public Entity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    public UUID getId() { return id; }
    public long getCreatedAt() { return createdAt; }
    public long getUpdatedAt() { return updatedAt; }
}

