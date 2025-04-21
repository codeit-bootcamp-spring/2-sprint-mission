package com.sprint.mission.discodeit.mapper.Impl;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapperImpl implements MessageMapper {
    private final BinaryContentMapper binaryContentMapper;
    private final UserMapper userMapper;

    @Override
    public MessageDto toDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannel().getId(),
                userMapper.toDto(message.getAuthor()),
                message.getAttachments().stream()
                        .map(binaryContentMapper::toDto)
                        .toList()
        );
    }
}