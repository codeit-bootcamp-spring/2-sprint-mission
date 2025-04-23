package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

  @Column(nullable = false, columnDefinition = "text")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "channel_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_message_channel")
  )
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "author_id",
      foreignKey = @ForeignKey(name = "fk_message_author")
  )
  private User author;

  @ManyToMany
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "fk_message_attachments_message")),
      inverseJoinColumns = @JoinColumn(name = "attachment_id", foreignKey = @ForeignKey(name = "fk_message_attachments_attachment"))
  )
  private List<BinaryContent> attachments;

  public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
    this.content = content;
    this.channel = channel;
    this.author = author;
    this.attachments = attachments;
  }

  public void update(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
    }
  }
}
