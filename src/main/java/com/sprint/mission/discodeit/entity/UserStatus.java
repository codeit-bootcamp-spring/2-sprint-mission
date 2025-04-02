package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final int DEFAULT_DURATION = 5 * 60 * 1000;
  private final UUID id;
  private final UUID userId;
  private final Instant createdAt;
  private Instant updatedAt;
  private Instant lastLoginAt;

  public UserStatus(UUID userId) {
    this.id = UUID.randomUUID();
    this.userId = userId;
    this.createdAt = ZonedDateTime.now().toInstant();
    this.updatedAt = createdAt;
    this.lastLoginAt = Instant.ofEpochSecond(0);
  }

  public void updateLastLoginAt() {
    this.lastLoginAt = ZonedDateTime.now().toInstant();
    updateLastModified();
  }

  public boolean isLogin(Instant now) {
    return Duration.between(lastLoginAt, now).toMillis() <= DEFAULT_DURATION;
  }

  private void updateLastModified() {
    this.updatedAt = ZonedDateTime.now().toInstant();
  }
}
