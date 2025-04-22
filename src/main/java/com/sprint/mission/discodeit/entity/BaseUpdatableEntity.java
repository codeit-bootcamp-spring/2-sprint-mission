package com.sprint.mission.discodeit.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@MappedSuperclass
public class BaseUpdatableEntity extends BaseEntity {
    @LastModifiedDate
    protected Instant updatedAt;
}
