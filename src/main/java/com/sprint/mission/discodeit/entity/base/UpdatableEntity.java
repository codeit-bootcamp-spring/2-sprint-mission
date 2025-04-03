package com.sprint.mission.discodeit.entity.base;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
public abstract class UpdatableEntity extends BaseEntity {

    protected Instant updatedAt;

    protected UpdatableEntity() {
        super();
        this.updatedAt = this.createdAt;
    }
}
