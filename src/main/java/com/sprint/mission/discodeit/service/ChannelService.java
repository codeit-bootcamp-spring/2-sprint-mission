package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDto.Response createPrivateChannel(ChannelDto.CreatePrivate dto);
    ChannelDto.Response createPublicChannel(ChannelDto.CreatePublic dto);
    ChannelDto.Response findById(UUID channelId);
    List<ChannelDto.Response> findAllByUserId(UUID userId);
    List<ChannelDto.Response> findAllPublicChannels();
    ChannelDto.Response updateChannel(ChannelDto.Update dto, UUID ownerId);
    boolean deleteChannel(UUID channelId,UUID ownerId);
    List<ChannelDto.Response> getAccessibleChannels(UUID userId);
}

