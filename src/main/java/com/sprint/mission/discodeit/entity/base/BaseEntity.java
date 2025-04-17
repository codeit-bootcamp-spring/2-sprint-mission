package com.sprint.mission.discodeit.entity.base;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

@Getter
public abstract class BaseEntity {

  protected UUID id;
  @CreatedDate
  protected Instant createdAt;

  public BaseEntity() {
    this.id = UUID.randomUUID();
  }
}
