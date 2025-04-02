package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UUID authorId,
    List<UUID> attachmentIds

) {

  public static MessageResponse of(Message message) {
    return new MessageResponse(message.getId(), message.getCreatedAt(),
        message.getUpdatedAt(), message.getContent(), message.getChannelId(), message.getAuthorId(),
        message.getAttachmentIds());
  }
}
