package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user_statuses")
@Entity
@Getter
@NoArgsConstructor
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt = Instant.now();

  public void updateLastLoginTime(Instant lastLoginTime) {
    this.lastActiveAt = lastLoginTime;
  }

  public boolean isLastStatus() {
    if (lastActiveAt == null || lastActiveAt.isBefore(Instant.now().minusSeconds(300))) {
      return false;
    }
    return true;
  }

  public UserStatus(User user) {
    this.user = user;
    this.lastActiveAt = Instant.now();
  }
}
