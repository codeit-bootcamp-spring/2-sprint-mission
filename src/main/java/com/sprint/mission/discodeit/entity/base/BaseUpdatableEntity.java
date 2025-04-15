package com.sprint.mission.discodeit.entity.base;

import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class BaseUpdatableEntity extends BaseEntity {

  protected Instant updatedAt;

  protected BaseUpdatableEntity() {
    super();
    this.updatedAt = this.createdAt;
  }

  public void markUpdated() {
    this.updatedAt = Instant.now();
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
