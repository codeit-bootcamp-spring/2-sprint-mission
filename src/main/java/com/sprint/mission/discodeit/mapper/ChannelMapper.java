package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MessageMapper.class, ReadStatusMapper.class,
    UserMapper.class})
public interface ChannelMapper {

  ChannelDto toDto(Channel channel, List<UserDto> participants, Instant lastMessageCreatedAt);
}
