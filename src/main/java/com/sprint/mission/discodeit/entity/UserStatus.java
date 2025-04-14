package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserStatus extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final int ONLINE_THRESHOLD = 300;
  private final UUID userid;
  private Instant lastActiveAt;

  @Builder.Default
  private boolean status = true;

  public UserStatus(UUID userid, Instant lastActiveAt) {
    super();
    this.userid = userid;
    this.status = true;
    this.lastActiveAt = lastActiveAt;
  }

  public boolean isUserOnline() {
    return Duration.between(getUpdatedAt(), Instant.now()).getSeconds() < ONLINE_THRESHOLD;

  }

  public void updateLastActiveAt() {
    updateTimestamp();
  }

}
