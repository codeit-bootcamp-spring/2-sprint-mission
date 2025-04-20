package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_status_user"))
  private User user;

  @Column(nullable = false)
  private Instant lastActiveAt;

  public UserStatus(User user, Instant lastActiveAt) {
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }

  public void update(Instant newLastActiveAt) {
    if (newLastActiveAt != null) {
      this.lastActiveAt = newLastActiveAt;
    }
  }

  public boolean isOnline() {
    return lastActiveAt != null &&
        lastActiveAt.isAfter(Instant.now().minus(Duration.ofMinutes(5)));
  }
}