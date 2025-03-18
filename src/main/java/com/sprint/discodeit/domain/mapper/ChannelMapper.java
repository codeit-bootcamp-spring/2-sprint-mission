package com.sprint.discodeit.domain.mapper;

import com.sprint.discodeit.domain.dto.channelDto.ChannelCreateRequestDto;
import com.sprint.discodeit.domain.entity.Channel;

public class ChannelMapper {

    public static Channel toChannelMapper(ChannelCreateRequestDto channelCreateRequestDto) {
        return Channel.builder()
                .type(channelCreateRequestDto.type())
                .name(channelCreateRequestDto.name())
                .description(channelCreateRequestDto.description())
                .build();

    }
}
