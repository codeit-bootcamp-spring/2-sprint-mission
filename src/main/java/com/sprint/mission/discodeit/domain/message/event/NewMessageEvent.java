package com.sprint.mission.discodeit.domain.message.event;

import com.sprint.mission.discodeit.domain.message.dto.MessageDto;
import java.time.Instant;

public record NewMessageEvent(
    Instant createdAt,
    MessageDto messageDto
) {

  public NewMessageEvent(MessageDto messageDto) {
    this(Instant.now(), messageDto);
  }
} 