package com.sprint.mission.discodeit.entity.message;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseUpdatableEntity {

  private String content;

  private UUID channelId;
  private UUID authorId;
  private List<UUID> attachmentIds;

  public Message(String content, UUID channelId, UUID authorId) {
    this(content, channelId, authorId, new ArrayList<>());
  }

  public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
    super();

    this.content = content;
    this.channelId = channelId;
    this.authorId = authorId;
    this.attachmentIds = new ArrayList<>(attachmentIds);
  }

  public void update(String newContent) {
    boolean anyValueUpdated = false;
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      markUpdated();
    }
  }

  public void addAttachment(UUID attachmentId) {
    if (attachmentId == null) {
      throw new IllegalArgumentException("attachmentId가 null입니다.");
    }
    attachmentIds.add(attachmentId);
    markUpdated();
  }

  public void removeAttachment(UUID attachmentId) {
    if (this.attachmentIds.remove(attachmentId)) {
      markUpdated();
    }
  }

  @Override
  public String toString() {
    return "Message{" +
        "newContent='" + content + '\'' +
        ", channelId=" + channelId +
        ", authorId=" + authorId +
        ", attachmentIds=" + attachmentIds +
        ", updatedAt=" + updatedAt +
        ", id=" + id +
        ", createdAt=" + createdAt +
        '}';
  }
}
