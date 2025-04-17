package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
public class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  @Column
  private Instant updatedAt;
  
  public Instant getUpdatedAt() {
    return updatedAt;
  }

}
