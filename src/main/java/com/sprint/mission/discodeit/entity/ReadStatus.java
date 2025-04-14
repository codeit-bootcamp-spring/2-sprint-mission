package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReadStatus extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID channelId;
  private final UUID userId;
  private Instant lastReadAt;

  public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
    super();
    this.userId = userId;
    this.channelId = channelId;
    if (lastReadAt != null) {
      this.lastReadAt = Instant.now();
    } else {
      this.lastReadAt = lastReadAt;
    }
  }

  public void updateLastAccessTime() {
    this.lastReadAt = Instant.now();
  }

  @Override
  public String toString() {
    return "ReadStatus{" +

        "id=" + getId() +
        ", channelId=" + channelId +
        ", userIds=" + userId +
        ", readTime=" + lastReadAt +
        '}';
  }
}
