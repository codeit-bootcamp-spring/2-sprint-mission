package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

  @Id
  @GeneratedValue
  private UUID id;

  @CreationTimestamp
  @Column(updatable = false, nullable = false)
  private Instant createdAt;
}
