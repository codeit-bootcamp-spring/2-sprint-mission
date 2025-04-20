package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_statuses")
@Getter
@NoArgsConstructor
public class UserStatus extends BaseUpdatableEntity {

  private static final int ONLINE_TIME = 5;
  //
  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt;

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  public UserStatus(User user, Instant lastActiveAt) {
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }

  public void update(Instant newLastActiveAt) {
    if (newLastActiveAt != null && !newLastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = newLastActiveAt;
    }
  }

  public boolean isOnline() {
    Duration duration = Duration.between(lastActiveAt, Instant.now());
    return duration.toMinutes() <= ONLINE_TIME;
  }
}
