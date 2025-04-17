package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID userId;
  private final UUID channelId;
  private Instant lastReadTime;

  public ReadStatus(UUID userId, UUID channelId, Instant lastReadTime) {
    super();
    this.userId = userId;
    this.channelId = channelId;
    this.lastReadTime = lastReadTime;
  }

  public void updateLastReadTime(Instant lastReadTime) {
    this.lastReadTime = lastReadTime;
    setUpdatedAt(Instant.now());
  }
}
