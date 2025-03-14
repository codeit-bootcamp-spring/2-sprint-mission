package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.entity.Message;
import java.util.UUID;

public record MessageDto(UUID messageId, String context, UUID channelId, UserDto user) {
    public static MessageDto fromEntity(Message message, UserDto userDto) {
        return new MessageDto(message.getId(), message.getContext(), message.getChannelId(), userDto);
    }
}
