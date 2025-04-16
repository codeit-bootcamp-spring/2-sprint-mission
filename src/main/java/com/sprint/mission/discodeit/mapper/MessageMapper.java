package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final BinaryContentMapper binaryContentMapper;
    private final UserMapper userMapper;

    public MessageDto toDto(Message message) {
        if (message == null) return null;

        List<BinaryContentDto> attachmentDtos = message.getAttachments().stream()
                .map(attachment -> binaryContentMapper.toDto(attachment.getAttachment()))
                .collect(Collectors.toList());

        return new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannel().getId(),
                userMapper.toDto(message.getAuthor()),
                attachmentDtos
        );
    }

    public List<MessageDto> toDtoList(List<Message> messages) {
        if (messages == null) {
            return Collections.emptyList();
        }

        return messages.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
