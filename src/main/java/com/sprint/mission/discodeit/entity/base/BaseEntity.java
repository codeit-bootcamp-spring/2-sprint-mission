package com.sprint.mission.discodeit.entity.base;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public abstract class BaseEntity {

  private static final long serialVersionUID = 1L;
  protected final UUID id;
  protected final Instant createdAt;

  public BaseEntity() {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
  }
}
