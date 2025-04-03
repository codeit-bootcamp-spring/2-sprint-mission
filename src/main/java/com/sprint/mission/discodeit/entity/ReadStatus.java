package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable, Identifiable {

  private final UUID id;
  private final UUID userId;
  private final UUID channelId;
  private final Instant createdAt;
  private Instant lastReadAt; // 마지막으로 메시지를 읽은 시간

  @Builder
  public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
    this.id = UUID.randomUUID();
    this.userId = userId;
    this.channelId = channelId;
    this.createdAt = Instant.now();
    this.lastReadAt = lastReadAt;
  }

  public void updateReadStatus() {
    this.lastReadAt = Instant.now();
  }
}
