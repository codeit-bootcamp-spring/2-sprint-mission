package com.sprint.mission.discodeit.core.status.entity;

import com.sprint.mission.discodeit.core.BaseUpdatableEntity;
import com.sprint.mission.discodeit.core.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "user_statuses")
@Entity
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Timestamp
  @Column(name = "last_active_at")
  private Instant lastActiveAt;

  private UserStatus(User user, Instant lastActiveAt) {
    super();
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }

  public static UserStatus create(User user, Instant lastActiveAt) {
    return new UserStatus(user, lastActiveAt);
  }

  public void update(Instant lastActiveAt) {
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
    }
  }

  public boolean isOnline() {
    Instant updatedAt = getUpdatedAt();
    if (updatedAt == null) {
      return false;
    }
    return Duration.between(updatedAt, Instant.now()).getSeconds() <= 300;
  }

}

