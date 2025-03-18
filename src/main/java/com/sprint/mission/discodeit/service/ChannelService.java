package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelReqDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelReqDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResDto createPublicChannel(CreateChannelReqDto.Public createChannelReqDto);
    ChannelResDto createPrivateChannel(CreateChannelReqDto.Private createChannelReqDto);
    ChannelResDto find(UUID channelId);
    List<ChannelResDto> findAll();
    List<ChannelResDto> findAllByUserId(UUID userId);
    ChannelResDto update(UUID channelId, UpdateChannelReqDto updateChannelReqDto);
    void delete(UUID channelId);
}
