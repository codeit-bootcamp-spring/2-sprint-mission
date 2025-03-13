package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    void createChannel(String channelName);
    Channel findChannel(UUID id);
    List<Channel> findAllChannel();
    void updateChannel(UUID uuid, String channelName);
    void deleteChannel(UUID id);
}
