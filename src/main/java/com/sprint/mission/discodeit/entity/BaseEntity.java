package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class BaseEntity {
    protected final UUID id;
    protected Long createAt;
    protected Long updateAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
        this.updateAt = createAt;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }
}

