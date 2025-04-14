package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;


@Getter
@MappedSuperclass
public abstract class BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Column(nullable = false, updatable = false)
  protected UUID id;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  protected Instant createdAt;

  public BaseEntity() {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
  }
}
