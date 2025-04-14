package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serializable;
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
public class ReadStatus extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID channelId;
  private final UUID userId;
  private Instant lastReadAt;

  public ReadStatus(UUID userId, UUID channelId) {
    super();
    this.userId = userId;
    this.channelId = channelId;
    this.lastReadAt = Instant.now();
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
