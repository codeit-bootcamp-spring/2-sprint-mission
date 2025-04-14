package com.sprint.mission.discodeit.entity.base;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

@Getter
public abstract class BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID id;

  @CreatedDate
  private Instant createdAt;
}
