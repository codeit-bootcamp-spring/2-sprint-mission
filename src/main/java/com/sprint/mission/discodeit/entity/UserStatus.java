package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.ToString;

@Getter
@ToString
public class UserStatus extends SharedEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID userId;
  private Instant lastActiveAt;

  public UserStatus(UUID userId, Instant lastActiveAt) {
    super();
    this.userId = userId;
    this.lastActiveAt = lastActiveAt;
  }

  public void updateLastActiveAt(Instant lastActiveAt) {
    this.lastActiveAt = lastActiveAt;
    setUpdatedAt(Instant.now());
  }

  public boolean isOnline() {
    return lastActiveAt != null && Duration.between(lastActiveAt, Instant.now()).toMinutes() < 5;
  }
}
