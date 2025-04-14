package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Message extends BaseUpdatableEntity implements Serializable, Identifiable {

  private static final long serialVersionUID = 1L;

  private List<BinaryContent> attachments;
  private String content;
  private Channel channel;
  private User author;

  @Builder
  public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
    this.attachments = attachments != null ? new ArrayList<>(attachments) : new ArrayList<>();
    this.content = content;
    this.channel = channel;
    this.author = author;
  }

  public void updateMessageInfo(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
    }
  }

  public void updateMessageAttachment(List<BinaryContent> attachments) {
    this.attachments = new ArrayList<>(attachments);
  }

}
