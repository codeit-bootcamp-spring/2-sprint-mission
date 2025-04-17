package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serial;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseUpdatableEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final User user;
  private final Channel channel;
  private Instant lastReadTime;

  public ReadStatus(User user, Channel channel, Instant lastReadTime) {
    super();
    this.user = user;
    this.channel = channel;
    this.lastReadTime = lastReadTime;
  }

  public void updateLastReadTime(Instant lastReadTime) {
    this.lastReadTime = lastReadTime;
    setUpdatedAt(Instant.now());
  }
}
