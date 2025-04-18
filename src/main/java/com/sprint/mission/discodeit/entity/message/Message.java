package com.sprint.mission.discodeit.entity.message;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter
public class Message extends BaseUpdatableEntity {

  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private User author;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachments;

  protected Message() {
  }

  public Message(String content, Channel channel, User author) {
    this(content, channel, author, new ArrayList<>());
  }

  public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
    super();

    this.content = content;
    this.channel = channel;
    this.author = author;
    this.attachments = new ArrayList<>(attachments);
  }

  public void update(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
    }
  }

  public void addAttachment(BinaryContent attachment) {
    if (attachment == null) {
      throw new IllegalArgumentException("attachment가 null입니다.");
    }
    attachments.add(attachment);
  }

  public void removeAttachment(BinaryContent attachment) {
    attachments.remove(attachment);
  }

}
