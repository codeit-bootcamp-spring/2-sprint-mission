package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublic(PublicChannelCreateRequestDto dto);
    Channel createPrivate(PrivateChannelCreateRequestDto dto);
    ChannelResponseDto find(UUID channelId);
    List<ChannelResponseDto> findAllByUserID(UUID userID);
    Channel update(ChannelUpdateRequestDto dto);
    void delete(UUID channelId);
}
