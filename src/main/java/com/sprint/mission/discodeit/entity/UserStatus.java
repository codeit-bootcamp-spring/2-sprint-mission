package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final int ONLINE_TIME = 5;
  //
  private UserStatusType type;
  private Instant activatedAt;
  private final UUID userId;

  public UserStatus(UUID userId, Instant activatedAt) {
    super();
    this.type = UserStatusType.OFFLINE;
    this.userId = userId;
    this.activatedAt = activatedAt;
  }

  public void update(Instant newActivatedAt) {
    boolean anyValueUpdated = false;
    if (newActivatedAt != null && !newActivatedAt.equals(activatedAt)) {
      this.activatedAt = newActivatedAt;
      anyValueUpdated = true;
    }
    if (anyValueUpdated) {
      update();
    }
  }

  public boolean isOnline() {
    Duration duration = Duration.between(activatedAt, Instant.now());
    if (duration.toMinutes() <= ONLINE_TIME) {
      if (type != UserStatusType.ONLINE) {
        type = UserStatusType.ONLINE;
      }
      return true;
    }
    if (type != UserStatusType.OFFLINE) {
      type = UserStatusType.OFFLINE;
    }
    return false;
  }
}
