package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.Getter;

import java.time.Instant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  protected UUID id;
  @CreatedDate
  protected Instant createdAt;

  protected BaseEntity() {
    this.id = UUID.randomUUID();
  }
}
