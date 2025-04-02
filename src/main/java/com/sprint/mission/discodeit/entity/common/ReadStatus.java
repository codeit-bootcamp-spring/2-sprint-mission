package com.sprint.mission.discodeit.entity.common;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity {

  private UUID userId;
  private UUID channelId;

  private Instant lastReadAt;

  public ReadStatus(UUID userId, UUID channelId) {
    super();

    this.userId = userId;
    this.channelId = channelId;
  }

  public void updateLastReadAt(Instant newLastReadAt) {
    this.lastReadAt = newLastReadAt;
  }

  @Override
  public String toString() {
    return "ReadStatus{" +
        "userId=" + userId +
        ", channelId=" + channelId +
        ", lastReadAt=" + lastReadAt +
        ", id=" + id +
        ", createdAt=" + createdAt +
        '}';
  }
}
