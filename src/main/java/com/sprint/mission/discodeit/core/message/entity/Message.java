package com.sprint.mission.discodeit.core.message.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Message implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final UUID userId;
  private final UUID channelId;

  private final Instant createdAt;
  private Instant updatedAt;

  private String content;

  private List<UUID> attachmentIds;

  private Message(UUID userId, UUID channelId, String content, List<UUID> attachmentIds) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();

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
      this.updatedAt = Instant.now();
    }
  }

//  public void update(String newText, List<UUID> newAttachmentIds) {
//    boolean anyValueUpdated = false;
//    if (newText != null && !newText.equals(content)) {
//      this.content = newText;
//      anyValueUpdated = true;
//    }
//    if (newAttachmentIds != null) {
//      this.attachmentIds = newAttachmentIds;
//      anyValueUpdated = true;
//    }
//    if (anyValueUpdated) {
//      this.updatedAt = Instant.now();
//    }
//  }

  //TODO. Message Validator 구현해야함
  public static class Validator {

    public static void validate(String text) {
      validateText(text);
    }

    public static void validateText(String text) {

    }
  }
}
