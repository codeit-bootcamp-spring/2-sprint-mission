package com.sprint.mission.discodeit.entity.base;

import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
public abstract class BaseUpdatableEntity extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @LastModifiedDate
  private Instant updatedAt;
}
