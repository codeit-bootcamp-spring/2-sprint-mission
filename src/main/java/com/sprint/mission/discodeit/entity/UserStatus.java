package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_statuses")
@Getter
@NoArgsConstructor
public class UserStatus extends BaseUpdatableEntity implements Serializable, Identifiable {

  private static final long serialVersionUID = 1L;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt; // 유저의 마지막 접속시간

  @Builder
  public UserStatus(User user, Instant lastActiveAt) {
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }

  public void updateUserStatus() {
    this.lastActiveAt = Instant.now();
  }

  public boolean isLoginUser() {
    if (Duration.between(this.lastActiveAt, Instant.now()).toMinutes() < 5) {
      return true;
    } else {
      return false;
    }
  }

}
