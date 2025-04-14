package com.sprint.mission.discodeit.core.message.entity;

import com.sprint.mission.discodeit.core.base.BaseUpdatableEntity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

  //TODO. Messages 연관관계 매핑해야 함, ManyToOne
  private final UUID userId;
  private final UUID channelId;

  @Lob
  private String content;

  //TODO. Messages 연관관계 매핑해야함, 컬렉션 타입
  private List<UUID> attachmentIds;

  private Message(UUID userId, UUID channelId, String content, List<UUID> attachmentIds) {
    super();

    this.userId = userId;
    this.channelId = channelId;

    this.content = content;
    this.attachmentIds = attachmentIds;
  }

  public static Message create(UUID userId, UUID channelId, String text, List<UUID> attachmentIds) {
    return new Message(userId, channelId, text, attachmentIds);
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
