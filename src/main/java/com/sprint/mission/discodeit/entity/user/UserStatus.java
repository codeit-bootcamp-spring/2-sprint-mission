package com.sprint.mission.discodeit.entity.user;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseUpdatableEntity {

  private static final long ONLINE_THRESHOLD_MINUTES = 5;

  private UUID userId;

  private Instant lastActiveAt;

  public UserStatus(UUID userId) {
    super();
    this.userId = userId;
  }

  public void updateLastActiveAt(Instant newLastActiveAt) {
    this.lastActiveAt = newLastActiveAt;
  }

  public boolean isOnline() {
    if (lastActiveAt == null) {
      return false;
    }
    long minutesAfterLogin = Duration.between(lastActiveAt, Instant.now()).toMinutes();
    return minutesAfterLogin < ONLINE_THRESHOLD_MINUTES;
  }

  @Override
  public String toString() {
    return "UserStatus{" +
        "userId=" + userId +
        ", lastLoginAt=" + lastActiveAt +
        ", id=" + id +
        ", createdAt=" + createdAt +
        '}';
  }
}
