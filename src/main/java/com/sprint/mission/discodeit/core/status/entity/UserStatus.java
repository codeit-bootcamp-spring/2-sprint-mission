package com.sprint.mission.discodeit.core.status.entity;

import com.sprint.mission.discodeit.core.base.BaseUpdatableEntity;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus extends BaseUpdatableEntity {

  private final UUID userId;
  private Instant lastActiveAt;

  private UserStatus(UUID userId, Instant lastActiveAt) {
    super();
    this.userId = userId;
    this.lastActiveAt = lastActiveAt;
  }

  public static UserStatus create(UUID userId, Instant lastActiveAt) {
    return new UserStatus(userId, lastActiveAt);
  }

  public void update(Instant lastActiveAt) {
    boolean anyValueUpdated = false;
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      super.updateTime();
    }
  }

  public void setOffline() {
    updateTime(Instant.now().minus(Duration.ofMinutes(6)));
  }

  public boolean isOnline() {
    return Duration.between(getUpdatedAt(), Instant.now()).getSeconds() <= 300;
  }

}
