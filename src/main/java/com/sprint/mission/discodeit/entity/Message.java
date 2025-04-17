package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serial;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseUpdatableEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String content;
  private Channel channel;
  private User author;
  private List<BinaryContent> attachments;

  public Message(String content, Channel channel, User author) {
    super();
    this.content = content;
    this.channel = channel;
    this.author = author;
  }

  public void setAttachmentIds(List<BinaryContent> attachments) {
    this.attachments = attachments;
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
