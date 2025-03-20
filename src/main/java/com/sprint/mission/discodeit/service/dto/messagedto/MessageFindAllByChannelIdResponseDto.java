package com.sprint.mission.discodeit.service.dto.messagedto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageFindAllByChannelIdResponseDto(
        UUID id,
        String message,
        UUID channelId,
        UUID senderId,
        List<UUID> attachmentIds

) {
    public static List<MessageFindAllByChannelIdResponseDto> fromChannel(List<Message> message) {
        return message.stream()
                .map(m -> new MessageFindAllByChannelIdResponseDto(
                        m.getId(),
                        m.getMessage(),
                        m.getChannelId(),
                        m.getSenderId(),
                        m.getAttachmentIds()
                ))
                .toList();
    }

}