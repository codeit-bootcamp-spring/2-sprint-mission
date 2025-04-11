package com.sprint.mission.discodeit.core.base;

import java.time.Instant;
import lombok.Getter;

@Getter
public abstract class BaseUpdatableEntity extends BaseEntity {

  private Instant updatedAt;

  public void updateTime() {
    updatedAt = Instant.now();
  }

  public void updateTime(Instant time) {
    updatedAt = time;
  }
}
