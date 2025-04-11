package com.sprint.mission.discodeit.core.message.entity;

import com.sprint.mission.discodeit.core.base.BaseUpdatableEntity;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Message extends BaseUpdatableEntity {

  private final UUID userId;
  private final UUID channelId;

  private String content;
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
    boolean anyValueUpdated = false;
    if (newText != null && !newText.equals(content)) {
      this.content = newText;
      anyValueUpdated = true;
    }
    if (anyValueUpdated) {
      super.updateTime();
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
