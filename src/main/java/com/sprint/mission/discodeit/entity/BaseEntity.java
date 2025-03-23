package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private Instant createdAt;
    @Setter
    private Instant updatedAt;

    public BaseEntity(UUID id, Instant createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt;
    }
}
