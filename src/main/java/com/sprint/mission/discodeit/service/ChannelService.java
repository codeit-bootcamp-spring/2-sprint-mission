package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.channelDto.ChannelResponse;
import com.sprint.mission.discodeit.service.channelDto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.channelDto.PublicChannelCreateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPrivateChannel(PrivateChannelCreateRequest request);
    Channel createPublicChannel(PublicChannelCreateRequest request);
    ChannelResponse find(UUID channelId);
    List<ChannelResponse> findAll(UUID userId);
    Channel update(ChannelUpdateRequest request);
    void delete(UUID channelId);
}
