package com.sprint.mission.discodeit.core.status.entity;

import com.sprint.mission.discodeit.core.base.BaseUpdatableEntity;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ReadStatus extends BaseUpdatableEntity {

  private final UUID userId;
  private final UUID channelId;

  private Instant lastReadAt;

  private ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
    super();
    this.userId = userId;
    this.channelId = channelId;
    this.lastReadAt = lastReadAt;
  }

  public static ReadStatus create(UUID userId, UUID channelId, Instant lastReadAt) {
    return new ReadStatus(userId, channelId, lastReadAt);
  }

  public void update(Instant newLastReadAt) {
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
    }
  }
}
