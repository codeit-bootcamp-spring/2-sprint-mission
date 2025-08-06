package com.sprint.mission.discodeit.domain.message.dto;

import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.storage.dto.BinaryContentDto;
import com.sprint.mission.discodeit.domain.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.user.dto.UserDto;
import com.sprint.mission.discodeit.domain.user.entity.User;
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
    User messageAuthor = message.getAuthor();
    List<BinaryContent> messageAttachments = message.getAttachments();
    return MessageDto.builder()
        .id(message.getId())
        .createdAt(message.getCreatedAt())
        .updatedAt(message.getUpdatedAt())
        .content(message.getContent())
        .channelId(message.getChannel().getId())
        .author(messageAuthor != null ? UserDto.from(messageAuthor) : null)
        .attachments(
            messageAttachments != null ? messageAttachments.stream().map(BinaryContentDto::from)
                .toList() : null)
        .build();
  }
}
