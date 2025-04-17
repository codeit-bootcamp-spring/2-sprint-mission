package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus extends BaseUpdatableEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final User user;
  private Instant lastActiveAt;
  private boolean online;

  public UserStatus(User user, Instant lastActiveAt) {
    super();
    this.user = user;
    this.lastActiveAt = lastActiveAt;
    this.online = isOnline();
  }

  public void updateLastActiveAt(Instant lastActiveAt) {
    boolean anyValueUpdated = false;
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      anyValueUpdated = true;
    }
    this.online = isOnline();

    if (anyValueUpdated) {
      setUpdatedAt(Instant.now());
    }
  }

  public boolean isOnline() {
    Instant fiveMinuteAgo = Instant.now().minusSeconds(5 * 60);

    return lastActiveAt.isAfter(fiveMinuteAgo);
  }
}
