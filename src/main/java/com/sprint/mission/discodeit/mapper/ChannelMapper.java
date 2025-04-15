package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChannelMapper {

    MessageRepository messageRepository;
    ReadStatusRepository readStatusRepository;
    UserMapper userMapper;

    ChannelDto toDto(Channel channel) {
        return null;
    }

}
