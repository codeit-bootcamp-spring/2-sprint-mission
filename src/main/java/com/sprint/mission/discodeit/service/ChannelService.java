package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelFindResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.channel.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublicChannel(PublicChannelCreateRequestDto requestDto);
    Channel createPrivateChannel(PrivateChannelCreateRequestDto requestDto);
    ChannelFindResponseDto find(UUID channelId);
    List<ChannelFindResponseDto> findAllByUserId(UUID userId);
    Channel update(ChannelUpdateRequestDto channelUpdateRequestDto);
    void delete(UUID channelId);
}
