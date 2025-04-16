package com.sprint.mission.discodeit.core.message.usecase.dto;

import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentResult;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Message Item")
public record MessageResult(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UserResult author,
    List<BinaryContentResult> attachments
) {

  public static MessageResult create(Message message, User user) {
    return MessageResult.builder()
        .id(message.getId())
        .createdAt(message.getCreatedAt())
        .updatedAt(message.getUpdatedAt())
        .content(message.getContent())
        .channelId(message.getChannel().getId())
        .author(UserResult.create(user, user.getUserStatus().isOnline()))
        .attachments(
            message.getAttachment().stream().map(BinaryContentResult::create).toList())
        .build();
  }
}
