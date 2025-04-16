package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

  private String content;

  @ManyToOne(optional = false)
  @JoinColumn(name = "channel_id", foreignKey = @ForeignKey(name = "fk_message_channel"))
  private Channel channel;

  @ManyToOne(optional = true)
  @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_message_author"))
  private User author;

  @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<MessageAttachment> attachments = new ArrayList<>();


  public Message(String content, Channel channel, User author) {
    this.content = content;
    this.channel = channel;
    this.author = author;
  }

  public void update(String newContent) {
    boolean anyValueUpdated = false;
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      anyValueUpdated = true;
    }

  }
}
