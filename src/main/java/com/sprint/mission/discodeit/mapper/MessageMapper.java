package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageMapper {

    BinaryContentMapper binaryContentMapper;
    UserMapper userMapper;

    MessageDto toDto(Message message) {
        return null;
    }

}
