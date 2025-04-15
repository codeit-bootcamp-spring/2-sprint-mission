package com.sprint.mission.discodeit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  // orphanRemoval이 필요한가? 혹시라도 User가 삭제되지 않는데 status를 끊을 상황이 발생하는가?
  @JoinColumn(name = "user_id", unique = true, nullable = false)
  private User user;

  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt;

  public UserStatus() {
    super();
  }

  public UserStatus(User user, Instant lastActiveAt) {
    super();
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }

  public void update(Instant lastActiveAt) {
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
    }
  }

  public Boolean isOnline() {
    Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
    return lastActiveAt.isAfter(instantFiveMinutesAgo);
  }
}
