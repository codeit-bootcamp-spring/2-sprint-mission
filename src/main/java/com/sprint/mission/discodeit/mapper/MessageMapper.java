package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.file.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final UserMapper userMapper;
    private final BinaryContentMapper binaryContentMapper;

    public MessageDto toDto(Message message) {
        if (message == null) {
            return null;
        }

        UUID channelId = message.getChannel() != null ? message.getChannel().getId() : null;
        UserDto authorDto = userMapper.toDto(message.getAuthor());

        List<BinaryContentDto> attachmentDtos = message.getAttachments().stream()
            .map(binaryContentMapper::toDto)
            .collect(Collectors.toList());

        return new MessageDto(
            message.getId(),
            message.getCreatedAt(),
            message.getUpdatedAt(),
            message.getContent(),
            channelId,
            authorDto,
            attachmentDtos
        );
    }
}
