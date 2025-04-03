package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class UserStatus extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private UUID userUUID;

  @Builder.Default
  private Instant lastLoginTime = null;

  public void updateLastLoginTime(Instant lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
    super.updateTime();
  }

  public boolean isLastStatus() {
    super.updateTime();
    if (lastLoginTime == null || lastLoginTime.isBefore(Instant.now().minusSeconds(300))) {
      return false;
    }
    return true;
  }
}
