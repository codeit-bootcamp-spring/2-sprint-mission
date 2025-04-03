package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID id;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  //
  private String content;
  //
  private UUID getChannelId;
  private UUID authorId;
  private List<UUID> attachmentIds;

  public Message(String content, UUID getChannelId, UUID authorId, List<UUID> attachmentIds) {
    this.id = UUID.randomUUID();
    this.createdAt = OffsetDateTime.now();
    //
    this.content = content;
    this.getChannelId = getChannelId;
    this.authorId = authorId;
    this.attachmentIds = attachmentIds;
  }

  public void update(String newContent) {
    boolean anyValueUpdated = false;
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = OffsetDateTime.now();
    }
  }
}
