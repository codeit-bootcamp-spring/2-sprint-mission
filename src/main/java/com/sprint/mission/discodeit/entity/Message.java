package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
public class Message extends BaseUpdatableEntity implements Serializable, Identifiable {

  private static final long serialVersionUID = 1L;

  private List<UUID> attachmentIds;
  private String content;
  private UUID channelId;
  private UUID authorId;

  @Builder
  public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
    this.attachmentIds = attachmentIds != null ? new ArrayList<>(attachmentIds) : new ArrayList<>();
    this.content = content;
    this.channelId = channelId;
    this.authorId = authorId;
  }

  public void updateMessageInfo(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
    }
  }

  public void updateMessageAttachment(List<UUID> attachmentIds) {
    this.attachmentIds = new ArrayList<>(attachmentIds);
  }

}
