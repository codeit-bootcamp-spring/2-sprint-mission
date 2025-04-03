package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public class _UserStatus implements Serializable {

  private static final long serialVersionUID = 1L;
  private UUID id;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  //
  private UUID userId;
  private OffsetDateTime lastActiveAt;

  public _UserStatus(UUID userId, OffsetDateTime lastActiveAt) {
    this.id = UUID.randomUUID();
    this.createdAt = OffsetDateTime.now();
    //
    this.userId = userId;
    this.lastActiveAt = lastActiveAt;
  }

  public void update(OffsetDateTime lastActiveAt) {
    boolean anyValueUpdated = false;
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = OffsetDateTime.now();
    }
  }

  public Boolean isOnline() {
    OffsetDateTime OffsetDateTimeFiveMinutesAgo = OffsetDateTime.now().minus(Duration.ofMinutes(5));

    return lastActiveAt.isAfter(OffsetDateTimeFiveMinutesAgo);
  }
}
