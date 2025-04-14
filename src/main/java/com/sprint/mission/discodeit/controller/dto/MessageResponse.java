package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.service.dto.user.UserResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UserResponse author,
    List<BinaryContentResponse> attachments

) {

  public static MessageResponse of(Message message, UserResponse author,
      List<BinaryContentResponse> attachments) {
    return new MessageResponse(message.getId(), message.getCreatedAt(),
        message.getUpdatedAt(), message.getContent(), message.getChannelId(), author, attachments
    );
  }
}
