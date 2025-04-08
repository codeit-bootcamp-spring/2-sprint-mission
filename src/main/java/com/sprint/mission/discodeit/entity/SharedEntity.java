package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class SharedEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  protected final UUID id;
  protected Instant createdAt;
  protected Instant updatedAt;

  public SharedEntity() {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = Instant.now();
  }
}
