package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseUpdatableEntity implements Serializable, Identifiable {

  private static final long serialVersionUID = 1L;
  private User user;
  private Channel channel;
  private Instant lastReadAt; // 마지막으로 메시지를 읽은 시간

  @Builder
  public ReadStatus(User user, Channel channel, Instant lastReadAt) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
  }

  public void updateReadStatus(Instant newLastReadAt) {
    this.lastReadAt = newLastReadAt;
  }
}
