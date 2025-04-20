package com.sprint.mission.discodeit.Mapper;

import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final BinaryContentMapper binaryContentMapper;
    private final UserMapper userMapper;

    public MessageDto toDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .content(message.getContent())
                .channelId(message.getChannel().getId())
                .author(userMapper.toDto(message.getAuthor()))
                .attachments(
                        message.getAttachments().stream()
                                .map(messageAttachment -> binaryContentMapper.toDto(messageAttachment.getAttachment()))
                                .collect(Collectors.toList())
                )
                .build();
    }
}
