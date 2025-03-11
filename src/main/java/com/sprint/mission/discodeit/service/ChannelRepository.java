package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository {
    Channel register(Channel channel);
    Set<UUID> allChannelIdList();
    Optional<Channel> findChannelByName(String channelName);
    Optional<String> findChannelNameById(UUID channelId);
    Optional<Channel> findChannelById(UUID channelId);
    boolean deleteChannel(UUID channelId);
    boolean updateChannel(Channel channel);
}


