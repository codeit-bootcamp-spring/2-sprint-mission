package com.sprint.mission.discodeit.entity.base;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  protected UUID id;
  protected Instant createdAt;
  protected Instant updatedAt;

  protected BaseEntity() {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt;
  }
}
