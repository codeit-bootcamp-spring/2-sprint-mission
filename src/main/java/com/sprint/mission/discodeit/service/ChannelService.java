package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublicChannel(PublicChannelCreateRequest request);
    Channel createPrivateChannel(PrivateChannelCreateRequest request);
    ChannelFindResponse find(UUID channelId);
    List<ChannelFindResponse> findAllByUserId(UUID userId);
    Channel update(ChannelUpdateRequest request);
    void delete(UUID channelId);
}
