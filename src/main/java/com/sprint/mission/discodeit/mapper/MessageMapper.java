package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final BinaryContentMapper binaryContentMapper;
    private final UserMapper userMapper;

    public MessageResponseDto toDto(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannel().getId(),
                userMapper.toDto(message.getAuthor()),
                message.getAttachments().stream().map(binaryContentMapper::toDto).toList()
        );
    }
}
