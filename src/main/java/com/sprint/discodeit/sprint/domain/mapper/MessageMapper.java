package com.sprint.discodeit.sprint.domain.mapper;

import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelMessageResponseDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.sprint.domain.entity.Message;
import java.util.List;
import java.util.UUID;

public class MessageMapper {

    public static Message toMessage(MessageRequestDto messageRequestDto) {
        return Message.builder()
                .content(messageRequestDto.content())
                .content(messageRequestDto.content())
                .build();
    }

    public static ChannelMessageResponseDto toChannelMessageResponseDto(Message message) {
        return new ChannelMessageResponseDto(message.getId(), message.getContent(), message.getUsers().getUsername(), message.getCreatedAt());
    }
}
