package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class BaseEntity {
    protected final UUID id;
    protected Long createAt;
    protected Long updateAt;

    public BaseEntity(UUID id, Long createAt, Long updateAt) {
        this.id = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
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
