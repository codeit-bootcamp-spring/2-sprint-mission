package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serializable;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReadStatus extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private final Channel channel;
  private final User user;
  private Instant lastReadAt;

  public ReadStatus(Channel channel, User user, Instant lastReadAt) {
    super();
    this.channel = channel;
    this.user = user;
    this.lastReadAt = lastReadAt;
  }

  public void updateLastAccessTime() {
    this.lastReadAt = Instant.now();
  }

  @Override
  public String toString() {
    return "ReadStatus{" +

        "id=" + getId() +
        ", channel=" + channel +
        ", user=" + user +
        ", readTime=" + lastReadAt +
        '}';
  }
}
