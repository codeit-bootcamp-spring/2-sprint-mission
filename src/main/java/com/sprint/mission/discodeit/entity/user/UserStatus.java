package com.sprint.mission.discodeit.entity.user;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "user_statuses")
@Getter
public class UserStatus extends BaseUpdatableEntity {

  private static final long ONLINE_THRESHOLD_MINUTES = 5;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  private Instant lastActiveAt;

  protected UserStatus() {
  }

  public UserStatus(User user) {
    super();
    this.user = user;
    this.lastActiveAt = Instant.now();
  }

  public void updateLastActiveAt(Instant newLastActiveAt) {
    this.lastActiveAt = newLastActiveAt;
  }

  public boolean isOnline() {
    long minutesAfterLogin = Duration.between(lastActiveAt, Instant.now()).toMinutes();
    return minutesAfterLogin < ONLINE_THRESHOLD_MINUTES;
  }

}
