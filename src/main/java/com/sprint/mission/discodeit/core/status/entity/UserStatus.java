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

  public final Instant createdAt;
  public Instant updatedAt;

  private final UUID userId;
  private Instant lastActiveAt;

  private UserStatus(UUID userStatusId, UUID userId, Instant createdAt, Instant lastActiveAt) {
    this.userStatusId = userStatusId;
    this.userId = userId;
    this.createdAt = createdAt;
    this.updatedAt = createdAt;
    this.lastActiveAt = lastActiveAt;
  }

  public static UserStatus create(UUID userId, Instant lastActiveAt) {
    return new UserStatus(UUID.randomUUID(), userId, Instant.now(), lastActiveAt);
  }

  public void update(Instant lastActiveAt) {
    boolean anyValueUpdated = false;
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }

  public void setOffline() {
    this.updatedAt = Instant.now().minus(Duration.ofMinutes(6));
  }

  public boolean isOnline() {
    return Duration.between(updatedAt, Instant.now()).getSeconds() <= 300;
  }

}
