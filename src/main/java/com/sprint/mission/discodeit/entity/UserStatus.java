package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

@Getter
public class UserStatus extends BaseUpdatableEntity implements Serializable, Identifiable {

  private static final long serialVersionUID = 1L;
  private User user;
  private Instant lastActiveAt; // 유저의 마지막 접속시간

  @Builder
  public UserStatus(User user, Instant lastActiveAt) {
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }

  public void updateUserStatus() {
    this.lastActiveAt = Instant.now();
  }

  public boolean isLoginUser() {
    if (Duration.between(this.lastActiveAt, Instant.now()).toMinutes() < 5) {
      return true;
    } else {
      return false;
    }
  }

}
