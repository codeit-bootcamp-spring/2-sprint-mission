package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Lob
  private String content;

  @ManyToOne
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;

  @Builder.Default
  @ManyToMany
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachments = new ArrayList<>();

  public Message(User author, Channel channel, String content) {
    super();
    this.author = author;
    this.channel = channel;
    this.content = content;
  }

  protected Message() {
  }

  public void updateContent(String content) {
    this.content = content;
    updateTimestamp();
  }

  public void addAttachment(BinaryContent attachment) {
    this.attachments.add(attachment);
  }

  @Override
  public String toString() {
    return "Message{" +
        "author= " + author + '\'' +
        ", sendTime= " + getCreatedAt() + '\'' +
        ", channel= " + channel + '\'' +
        ", content= '" + content + '\'' +
        '}';
  }
}
