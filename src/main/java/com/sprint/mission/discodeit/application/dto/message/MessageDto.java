package com.sprint.mission.discodeit.application.dto.message;

import com.sprint.mission.discodeit.application.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(UUID messageId, String context, Instant createdAt, UUID channelId, List<UUID> attachmentIds,
                         UserDto user) {
    public static MessageDto fromEntity(Message message, UserDto userDto) {
        return new MessageDto(message.getId(), message.getContext(), message.getCreatedAt(), message.getChannelId(), message.getAttachmentIds(), userDto);
    }
}
