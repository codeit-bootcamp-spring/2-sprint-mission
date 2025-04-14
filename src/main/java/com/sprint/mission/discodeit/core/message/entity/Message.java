package com.sprint.mission.discodeit.core.message.entity;

import com.sprint.mission.discodeit.core.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @Lob
  private String content;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachmentIds;

  private Message(User author, Channel channel, String content, List<BinaryContent> attachmentIds) {
    super();

    this.author = author;
    this.channel = channel;

    this.content = content;
    this.attachmentIds = attachmentIds;
  }

  public static Message create(User user, Channel channel, String text,
      List<BinaryContent> attachmentIds) {
    return new Message(user, channel, text, attachmentIds);
  }

  public void update(String newText) {
    if (newText != null && !newText.equals(content)) {
      this.content = newText;
    }
  }

  //TODO. Message Validator 구현해야함
  public static class Validator {

    public static void validate(String text) {
      validateText(text);
    }

    public static void validateText(String text) {

    }
  }
}
