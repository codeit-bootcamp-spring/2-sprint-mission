package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ChannelRepository {
    boolean register(Channel channel);
    Optional<Channel> findById(UUID id);
    Optional<Channel> findByName(String name);
    Set<UUID> allChannelIdList();
    Optional<String> findChannelNameById(UUID channelId);
    boolean deleteChannel(UUID id);
    boolean updateChannel(Channel channel);
}


