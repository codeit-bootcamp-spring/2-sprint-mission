package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserStatus extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private User user;

  @Builder.Default
  private Instant lastActiveAt = null;

  public void updateLastLoginTime(Instant lastLoginTime) {
    this.lastActiveAt = lastLoginTime;
  }

  public boolean isLastStatus() {
    if (lastActiveAt == null || lastActiveAt.isBefore(Instant.now().minusSeconds(300))) {
      return false;
    }
    return true;
  }
}
