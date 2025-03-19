package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel createPrivateChannel(PrivateChannelCreateRequest request);
    Channel createPublicChannel(PublicChannelCreateRequest request);
    Optional<Channel> getChannelById(UUID channelId);
    List<Channel> getChannelsByName(String name);
    List<Channel> getAllChannels();
    void updateChannel(UUID channelId, String newName);
    void deleteChannel(UUID channelId);
}
