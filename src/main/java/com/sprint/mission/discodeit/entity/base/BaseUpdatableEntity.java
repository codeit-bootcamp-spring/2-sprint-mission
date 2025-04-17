package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@MappedSuperclass
public abstract class BaseUpdatableEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

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
