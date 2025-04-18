package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

public interface ChannelMapper {
    ChannelDto toDto(Channel channel);
}
