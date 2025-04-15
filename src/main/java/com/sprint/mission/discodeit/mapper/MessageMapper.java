package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    BinaryContentMapper binaryContentMapper;
    UserMapper userMapper;

    public MessageDto toDto(Message message) {
        return new MessageDto(
            message.getId(),
            message.getCreatedAt(),
            message.getUpdatedAt(),
            message.getContent(),
            message.getChannel().getId(),
            userMapper.toDto(message.getAuthor()),
            binaryContentMapper.toDto(message)
        );

    }

}
