package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
public class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  @Column(nullable = false)
  protected Instant updatedAt;

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  protected void setUpdatedAt() {
    this.updatedAt = Instant.now();
  }
}
