package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    public BaseEntity(UUID id, long createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt;
    }
}
