package com.sprint.mission.discodeit.core.message.dto;

import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.storage.dto.BinaryContentDto;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UserDto author,
    List<BinaryContentDto> attachments
) {

  public static MessageDto from(Message message) {
    User user = message.getAuthor();

    return MessageDto.builder()
        .id(message.getId())
        .createdAt(message.getCreatedAt())
        .updatedAt(message.getUpdatedAt())
        .content(message.getContent())
        .channelId(message.getChannel().getId())
        .author(UserDto.from(user))
        .attachments(
            message.getAttachment().stream().map(BinaryContentDto::create).toList())
        .build();
  }
}
