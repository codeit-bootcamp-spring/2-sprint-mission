package com.sprint.mission.discodeit.entity.base;

import ch.qos.logback.classic.spi.LoggingEventVO;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  @Column(columnDefinition = "BINARY(16)")
  protected UUID id;

  @CreationTimestamp
  @Column(updatable = false, nullable = false)
  protected Instant createdAt;

  //public abstract LoggingEventVO getType();
}
