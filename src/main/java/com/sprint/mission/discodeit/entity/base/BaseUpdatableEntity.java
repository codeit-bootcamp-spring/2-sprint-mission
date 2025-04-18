package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@MappedSuperclass
public abstract class BaseUpdatableEntity extends BaseEntity {
    @LastModifiedDate
    @Column(nullable = false)
    protected Instant updatedAt;

    public BaseUpdatableEntity() {
        super();
        this.updatedAt = this.createdAt;
    }

    public void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
