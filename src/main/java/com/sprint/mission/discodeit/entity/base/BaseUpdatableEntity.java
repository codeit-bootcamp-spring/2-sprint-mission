package com.sprint.mission.discodeit.entity.base;

import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

public abstract class BaseUpdatableEntity extends BaseEntity {
    @LastModifiedDate
    private Instant updatedAt;
}
