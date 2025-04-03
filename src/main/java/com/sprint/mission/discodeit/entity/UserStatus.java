package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable, Identifiable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final UUID userId;
  private final Instant createdAt;
  private Instant lastActiveAt; // 유저의 마지막 접속시간

  @Builder
  public UserStatus(UUID userId, Instant lastActiveAt) {
    this.id = UUID.randomUUID();
    this.userId = userId;
    this.createdAt = Instant.now();
    this.lastActiveAt = lastActiveAt;
  }

  public void updateUserStatus() {
    this.lastActiveAt = Instant.now();
  }

  public boolean isLoginUser() {
    if (Duration.between(lastActiveAt, Instant.now()).toMinutes() < 5) {
      return true;
    } else {
      return false;
    }
  }

}
