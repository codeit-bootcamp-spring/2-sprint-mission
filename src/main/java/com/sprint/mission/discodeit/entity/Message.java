package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class Message extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID authorId;  // User의 UUID 저장
  private final UUID channelId; // Channel의 UUID 저장
  private String content;
  private List<UUID> attachmentIds = new ArrayList<>();

  public Message(UUID authorId, UUID channelId, String content) {
    super();
    this.authorId = authorId;
    this.channelId = channelId;
    this.content = content;
    this.attachmentIds = new ArrayList<>();
  }

  public void updateContent(String content) {
    this.content = content;
    updateTimestamp();
  }

  public void addAttachment(UUID attachmentId) {
    this.attachmentIds.add(attachmentId);
  }

  @Override
  public String toString() {
    return "Message{" +
        "senderId= " + authorId + '\'' +
        ", sendTime= " + getCreatedAt() + '\'' +
        ", channelId= " + channelId + '\'' +
        ", content= '" + content + '\'' +
        '}';
  }
}
