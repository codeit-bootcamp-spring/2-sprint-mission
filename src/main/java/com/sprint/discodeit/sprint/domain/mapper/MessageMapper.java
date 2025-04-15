package com.sprint.discodeit.sprint.domain.mapper;

import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.sprint.domain.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

public class MessageMapper {

    public static Message toMessage(MessageRequestDto messageRequestDto) {
        return Message.builder()
                .content(messageRequestDto.content())
                .content(messageRequestDto.content())
                .build();
    }
}
