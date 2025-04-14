package com.sprint.mission.discodeit.core.base;

import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseUpdatableEntity extends BaseEntity {

  @Timestamp
  @LastModifiedDate
  private Instant updatedAt;

  public void updateTime(Instant time) {
    updatedAt = time;
  }
}
