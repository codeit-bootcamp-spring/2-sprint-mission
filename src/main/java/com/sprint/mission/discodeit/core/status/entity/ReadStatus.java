package com.sprint.mission.discodeit.core.status.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
public class ReadStatus implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final UUID readStatusId;
  private final UUID userId;
  private final UUID channelId;

  private final Instant createdAt;
  private Instant updatedAt;
  private Instant lastReadAt;

  private ReadStatus(UUID readStatusId, UUID userId, UUID channelId, Instant lastReadAt,
      Instant createdAt) {
    this.readStatusId = readStatusId;
    this.userId = userId;
    this.channelId = channelId;
    this.createdAt = createdAt;
    this.updatedAt = createdAt;
    this.lastReadAt = lastReadAt;
  }

  public static ReadStatus create(UUID userId, UUID channelId, Instant lastReadAt) {
    return new ReadStatus(UUID.randomUUID(), userId, channelId, lastReadAt, Instant.now());
  }

  public void update(Instant newLastReadAt) {
    boolean anyValueUpdated = false;
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }
}
