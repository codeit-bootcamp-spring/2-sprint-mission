package com.sprint.mission.discodeit.core.status.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final UUID userStatusId;
  private final UUID userId;

  public final Instant createdAt;
  public Instant updatedAt;

  private UserStatus(UUID userStatusId, UUID userId, Instant createdAt) {
    this.userStatusId = userStatusId;
    this.userId = userId;
    this.createdAt = createdAt;
    this.updatedAt = createdAt;
  }

  public static UserStatus create(UUID userId) {
    return new UserStatus(UUID.randomUUID(), userId, Instant.now());
  }

  public void updatedTime() {
    this.updatedAt = Instant.now();
  }

  public void setOffline() {
    this.updatedAt = Instant.now().minus(Duration.ofMinutes(6));
  }

  public boolean isOnline() {
    return Duration.between(updatedAt, Instant.now()).getSeconds() <= 300;
  }

}
