package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@MappedSuperclass
@Getter
public abstract class BaseUpdatableEntity extends BaseEntity {

  @UpdateTimestamp
  @Column(nullable = false)
  protected Instant updatedAt;
}
