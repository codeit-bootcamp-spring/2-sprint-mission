package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class BaseUpdatableEntity extends BaseEntity {
    @Column
    private Instant updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
