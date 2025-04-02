package com.sprint.mission.discodeit.core.message.usecase.crud.dto;

import com.sprint.mission.discodeit.core.message.entity.Message;
import lombok.Builder;

import java.time.Instant;

@Builder
public record MessageResult(
    String text,
    Instant createdAt
) {

  public static MessageResult create(Message message) {
    return MessageResult.builder()
        .text(message.getText())
        .createdAt(message.getCreatedAt())
        .build();
  }
}
