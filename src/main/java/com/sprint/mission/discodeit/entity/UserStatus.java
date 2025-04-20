package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"user"})
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne
  @MapsId
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Column(name = "last_avtive_at", nullable = false)
  private Instant lastActiveAt;

  public UserStatus(User user) {
    this.user = user;
    this.id = user.getId();
    this.lastActiveAt = Instant.EPOCH;
  }

  public boolean isOnline() {
    return isOnline(Instant.now());
  }

  public boolean isOnline(Instant now) {
    return lastActiveAt != null && Duration.between(lastActiveAt, now).toMinutes() < 5;
  }
}
