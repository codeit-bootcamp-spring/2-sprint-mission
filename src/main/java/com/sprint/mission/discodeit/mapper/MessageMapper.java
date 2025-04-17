package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.dto.response.MessageResponseDto;
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

    public MessageResponseDto toDto1(Message message) {
        return MessageResponseDto.builder()
                .id(message.getId())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .content(message.getContent())
                .channelId(message.getChannel().getId())
                .author(userMapper.toDto(message.getAuthor()))
                .attachments(message.getAttachments().stream().map(binaryContentMapper::toDto).toList())
                .build();

    }
}
