package com.sprint.mission.discodeit.core.base;

import jakarta.persistence.Column;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.time.Instant;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseUpdatableEntity extends BaseEntity {

  @Timestamp
  @LastModifiedDate
  @Column(name = "updated_at")
  private Instant updatedAt;

  public void updateTime(Instant time) {
    updatedAt = time;
  }
}
