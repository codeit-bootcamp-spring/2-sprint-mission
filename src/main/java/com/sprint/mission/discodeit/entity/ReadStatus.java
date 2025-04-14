package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadStatus extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private User user;
  private Channel channel;
  private Instant lastReadAt;

  public void updateLastReadAt(Instant lastReadAt) {
    this.lastReadAt = lastReadAt;
  }

  public boolean isRead(Instant lastMessageTime) {
    if (lastReadAt == null || lastReadAt.isBefore(lastMessageTime)) {
      return false;
    }
    return true;
  }
}
