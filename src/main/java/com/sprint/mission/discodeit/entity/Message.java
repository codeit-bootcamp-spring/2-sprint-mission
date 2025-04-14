package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class Message extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private final User author;
  private final Channel channel;
  private String content;
  private List<BinaryContent> attachments = new ArrayList<>();

  public Message(User author, Channel channel, String content) {
    super();
    this.author = author;
    this.channel = channel;
    this.content = content;
    this.attachments = new ArrayList<>();
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
