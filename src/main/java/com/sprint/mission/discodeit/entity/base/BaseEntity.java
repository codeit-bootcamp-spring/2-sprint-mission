package com.sprint.mission.discodeit.entity.base;

import java.util.UUID;
import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class BaseEntity {

  protected UUID id;
  protected Instant createdAt;

  protected BaseEntity() {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
  }
}
