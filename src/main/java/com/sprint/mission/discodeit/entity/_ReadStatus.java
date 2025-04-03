package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public class _ReadStatus implements Serializable {

  private static final long serialVersionUID = 1L;
  private UUID id;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  //
  private UUID userId;
  private UUID getChannelId;
  private OffsetDateTime lastReadAt;

  public _ReadStatus(UUID userId, UUID getChannelId, OffsetDateTime lastReadAt) {
    this.id = UUID.randomUUID();
    this.createdAt = OffsetDateTime.now();
    //
    this.userId = userId;
    this.getChannelId = getChannelId;
    this.lastReadAt = lastReadAt;
  }

  public void update(OffsetDateTime newLastReadAt) {
    boolean anyValueUpdated = false;
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = OffsetDateTime.now();
    }
  }
}
