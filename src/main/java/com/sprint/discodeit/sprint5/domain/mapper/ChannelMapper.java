package com.sprint.discodeit.sprint5.domain.mapper;

import com.sprint.discodeit.sprint5.domain.ChannelType;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.PrivateChannelCreateRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.PublicChannelCreateRequestDto;
import com.sprint.discodeit.sprint5.domain.entity.Channel;

public class ChannelMapper {

    public static Channel toPrviateChannel(PrivateChannelCreateRequestDto requestDto) {
        return Channel.builder()
                .type(ChannelType.PRIVATE)
                .name(requestDto.channelName())
                .description(requestDto.channelDescription())
                .build();

    }
    public static Channel toPublicChannel(PublicChannelCreateRequestDto requestDto) {
        return Channel.builder()
                .type(ChannelType.PRIVATE)
                .name(requestDto.channelName())
                .description(requestDto.channelDescription())
                .build();

    }
}
