package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "user_statuses")
@Entity
@NoArgsConstructor
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private Instant lastActiveAt;
  private boolean online;

  public UserStatus(User user, Instant lastActiveAt) {
    this.user = user;
    this.lastActiveAt = lastActiveAt;
    this.online = isOnline();
  }

  public void updateLastActiveAt(Instant lastActiveAt) {
    boolean anyValueUpdated = false;
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      anyValueUpdated = true;
    }
    this.online = isOnline();

    if (anyValueUpdated) {
      setUpdatedAt(Instant.now());
    }
  }

  public boolean isOnline() {
    Instant fiveMinuteAgo = Instant.now().minusSeconds(5 * 60);

    return lastActiveAt.isAfter(fiveMinuteAgo);
  }
}
