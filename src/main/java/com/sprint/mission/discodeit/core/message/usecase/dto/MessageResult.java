package com.sprint.mission.discodeit.core.message.usecase.dto;

import com.sprint.mission.discodeit.core.message.entity.Message;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

import java.time.Instant;

@Builder
public record MessageResult(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UUID authorId,
    List<UUID> attachmentIds
) {

  public static MessageResult create(Message message) {
    return MessageResult.builder()
        .id(message.getMessageId())
        .createdAt(message.getCreatedAt())
        .updatedAt(message.getUpdatedAt())
        .content(message.getText())
        .channelId(message.getChannelId())
        .authorId(message.getUserId())
        .build();
  }
}
