package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@Entity
@Table(name = "user_statuses")

public class UserStatus extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final int ONLINE_THRESHOLD = 300;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt;


  public UserStatus(User user, Instant lastActiveAt) {
    super();
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }

  public UserStatus() {
  }

  public boolean isUserOnline() {
    return Duration.between(getUpdatedAt(), Instant.now()).getSeconds() < ONLINE_THRESHOLD;

  }

  public void updateLastActiveAt() {
    lastActiveAt = Instant.now();

    updateTimestamp();
  }

}
