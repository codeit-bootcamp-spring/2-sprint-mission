package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // 채널 생성 (PUBLIC/PRIVATE 분리)
    ChannelDto.Response createPublicChannel(ChannelDto.CreatePublic dto);
    ChannelDto.Response createPrivateChannel(ChannelDto.CreatePrivate dto);
    ChannelDto.Response findById(UUID channelId);
    List<ChannelDto.Response> findAllByUserId(UUID userId);
    List<ChannelDto.Response> findAllPublicChannels();
    ChannelDto.Response updateChannel(ChannelDto.Update dto);
    boolean deleteChannel(UUID channelId);
}

