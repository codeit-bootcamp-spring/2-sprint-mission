package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class BaseEntity {

  private final UUID id;
  private final Instant createdAt; // 객체생성시간
  private Instant updatedAt; // 객체수정시간

  public BaseEntity() {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void update() {
    this.updatedAt = Instant.now();
  }

}
