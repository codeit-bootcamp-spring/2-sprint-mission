package com.sprint.mission.discodeit.adapter.inbound.message.dto;

import com.sprint.mission.discodeit.core.message.entity.Message;
import lombok.Builder;

import java.time.Instant;

@Builder
public record MessageFindDTO(
    String userName,
    String text,
    Instant createdAt
) {

  public static MessageFindDTO create(Message message) {
    return MessageFindDTO.builder()
        .userName(message.getUserName())
        .text(message.getText())
        .createdAt(message.getCreatedAt())
        .build();
  }
}
