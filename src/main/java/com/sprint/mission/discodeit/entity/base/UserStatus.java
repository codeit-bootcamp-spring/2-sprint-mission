package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;

@Entity
@Getter
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false, unique = true, foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
  private User user;

  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt;

  protected UserStatus() {
  }

  public UserStatus(User user) {
    this.user = user;
  }

  public void updateLastActiveAt() {
    this.lastActiveAt = Instant.now();
  }
}
