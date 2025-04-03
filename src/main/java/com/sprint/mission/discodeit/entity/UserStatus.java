package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import java.io.Serializable;

@Getter
public class UserStatus implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final UUID userId;
  private final Instant craetedAt;
  private Instant updatedAt;
  private Instant lastActiveAt;
  private boolean online;

  public UserStatus(UUID userId, Instant lastActiveAt) {
    this.id = UUID.randomUUID();
    this.userId = userId;
    this.craetedAt = Instant.now();
    this.updatedAt = Instant.now();
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
      this.updatedAt = Instant.now();
    }
  }

  public boolean isOnline() {
    Instant fiveMinuteAgo = Instant.now().minusSeconds(5 * 60);

    return lastActiveAt.isAfter(fiveMinuteAgo);
  }

}
