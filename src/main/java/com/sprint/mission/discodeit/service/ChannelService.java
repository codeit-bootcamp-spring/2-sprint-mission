package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel createPrivateChannel(PrivateChannelCreateRequest request);
    Channel createPublicChannel(PublicChannelCreateRequest request);
    Optional<ChannelResponse> getChannelById(UUID channelId);
    List<Channel> getChannelsByName(String name);
    List<ChannelResponse> findAllByUserId(UUID userId);
    void updateChannel(UpdateChannelRequest request);
    void deleteChannel(UUID channelId);
}
