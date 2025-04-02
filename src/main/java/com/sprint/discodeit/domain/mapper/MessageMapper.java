package com.sprint.discodeit.domain.mapper;

import com.sprint.discodeit.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.domain.entity.Message;
import java.util.List;
import java.util.UUID;

public class MessageMapper {

    public static Message toMessage(MessageRequestDto messageRequestDto, List<UUID> binaryContentId, UUID channelId) {
        return Message.builder()
                .content(messageRequestDto.content())
                .content(messageRequestDto.content())
                .channelId(channelId)
                .authorId(messageRequestDto.authorId())
                .attachmentIds(binaryContentId)
                .build();
    }
}
