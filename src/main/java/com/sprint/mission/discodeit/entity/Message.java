package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends SharedEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private String content;
  private final UUID authorId;
  private final UUID channelId;
  private final List<UUID> attachmentIds;

  public Message(String content, UUID authorId, UUID channelId, List<UUID> attachmentIds) {
    super();
    this.content = content;
    this.authorId = authorId;
    this.channelId = channelId;
    this.attachmentIds = attachmentIds != null ? attachmentIds : new ArrayList<>();
  }

  public void updateContent(String content) {
    this.content = content;
    setUpdatedAt(Instant.now());
  }

  public void addAttachment(UUID attachmentKey) {
    this.attachmentIds.add(attachmentKey);
  }

  @Override
  public String toString() {
    return String.format("\n key= %s\n content= %s\n createdAt= %s\n updatedAt= %s\n",
        id, content, createdAt, updatedAt);
  }
}
