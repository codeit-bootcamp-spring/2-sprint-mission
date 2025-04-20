package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {

  @Column(columnDefinition = "TEXT")
  private String content;

  @ManyToOne(optional = false)
  @JoinColumn(name = "channel_id", foreignKey = @ForeignKey(name = "fk_messages_channel"))
  private Channel channel;

  @ManyToOne
  @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_messages_author"))
  private User author;

  @ManyToMany
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachmentIds;

  public Message(String content, Channel channel, User author, List<BinaryContent> attachmentIds) {
    this.content = content;
    this.channel = channel;
    this.author = author;
    this.attachmentIds = attachmentIds;
  }

  public void update(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      super.setUpdatedAt();
    }
  }
}