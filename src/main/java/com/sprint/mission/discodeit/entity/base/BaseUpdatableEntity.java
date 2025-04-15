package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.Instant;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  protected Instant updatedAt;

  protected BaseUpdatableEntity() {
    super();
  }

  public void markUpdated() {
    this.updatedAt = Instant.now();
  }
}
