package com.sprint.discodeit.sprint.domain.mapper;

import com.sprint.discodeit.sprint.domain.ChannelType;
import com.sprint.discodeit.sprint.domain.dto.channelDto.PrivateChannelCreateRequestDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.PublicChannelCreateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.PrivateChannel;
import com.sprint.discodeit.sprint.domain.entity.PublicChannel;

public class ChannelMapper {

    public static PrivateChannel toPrviateChannel(PrivateChannelCreateRequestDto requestDto) {
        return PrivateChannel.create(requestDto.channelName(), requestDto.channelDescription());
    }
    public static PublicChannel toPublicChannel(PublicChannelCreateRequestDto requestDto) {
        return PublicChannel.create(requestDto.channelName(), requestDto.channelDescription());
    }
}
