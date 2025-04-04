package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class UserStatus extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private UUID userId;

  @Builder.Default
  private Instant lastActiveAt = null;

  public void updateLastLoginTime(Instant lastLoginTime) {
    this.lastActiveAt = lastLoginTime;
    super.updateTime();
  }

  public boolean isLastStatus() {
    super.updateTime();
    if (lastActiveAt == null || lastActiveAt.isBefore(Instant.now().minusSeconds(300))) {
      return false;
    }
    return true;
  }
}
