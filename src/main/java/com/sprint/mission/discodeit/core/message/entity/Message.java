package com.sprint.mission.discodeit.core.message.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class Message implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final UUID messageId;
  private final UUID userId;
  private final UUID channelId;

  private final Instant createdAt;
  private Instant updatedAt;

  private List<UUID> attachmentIds = new ArrayList<>();
  private String text;

  private Message(UUID userId, UUID channelId, UUID messageId, Instant createdAt, String text,
      List<UUID> attachmentIds) {
    this.userId = userId;
    this.channelId = channelId;
    this.messageId = messageId;
    this.createdAt = createdAt;
    this.updatedAt = createdAt;
    this.text = text;
    this.attachmentIds = attachmentIds;
  }

  public static Message create(UUID userId, UUID channelId, String text, List<UUID> attachmentIds) {
    return new Message(userId, channelId, UUID.randomUUID(), Instant.now(), text, attachmentIds);
  }

  public void update(String newText, List<UUID> newAttachmentIds) {
    boolean anyValueUpdated = false;
    if (newText != null && !newText.equals(text)) {
      this.text = newText;
      anyValueUpdated = true;
    }
    if (newAttachmentIds != null) {
      this.attachmentIds = newAttachmentIds;
      anyValueUpdated = true;
    }
    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
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
