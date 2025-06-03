package com.sprint.mission.discodeit.domain;

import static lombok.AccessLevel.PROTECTED;

import com.sprint.mission.discodeit.domain.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user_statuses")
@Getter
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Column(nullable = false)
  private Instant lastActiveAt;

  public static UserStatus create(User user, Instant lastActiveAt) {
    validate(user);
    return UserStatus.builder()
        .user(user)
        .lastActiveAt(lastActiveAt)
        .build();
  }

  public void update(Instant lastActiveAt) {
    if (lastActiveAt != null && lastActiveAt.isAfter(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
    }
  }

  public void updateByUserId() {
    this.lastActiveAt = Instant.now();
  }

  public boolean isOnline() {
    return lastActiveAt != null &&
        lastActiveAt.isAfter(Instant.now().minus(5, ChronoUnit.MINUTES));
  }


  /*******************************
   * Validation check
   *******************************/
  private static void validate(User user) {
    if (user == null) {
      throw new IllegalArgumentException("user 객체가 없습니다.");
    }
  }

}
