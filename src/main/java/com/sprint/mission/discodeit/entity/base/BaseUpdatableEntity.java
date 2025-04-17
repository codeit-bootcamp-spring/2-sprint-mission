package com.sprint.mission.discodeit.entity.base;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
public class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  protected Instant updatedAt;

  public BaseUpdatableEntity() {
    super();
  }
}
