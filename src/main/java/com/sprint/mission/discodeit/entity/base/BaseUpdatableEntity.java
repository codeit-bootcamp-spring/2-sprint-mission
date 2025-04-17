package com.sprint.mission.discodeit.entity.base;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseUpdatableEntity extends BaseEntity {

  protected Instant updatedAt;

  public BaseUpdatableEntity() {
    super();
    this.updatedAt = Instant.now();
  }
}
