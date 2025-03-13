package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    protected final UUID id;
    protected final long createdAt;
    protected long updatedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public void updateUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }
}
