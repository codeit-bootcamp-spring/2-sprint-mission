package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private String content;
  private UUID channelId;
  private UUID authorId;
  private List<UUID> attachmentIds;

  public Message(String content, UUID channelId, UUID authorId) {
    super();
    this.content = content;
    this.channelId = channelId;
    this.authorId = authorId;
  }

  public void setAttachmentIds(List<UUID> attachmentIds) {
    this.attachmentIds = attachmentIds;
    setUpdatedAt(Instant.now());
  }

  public void update(String newContent) {
    boolean anyValueUpdated = false;
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      setUpdatedAt(Instant.now());
    }
  }
}
