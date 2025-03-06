package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelService {
    void createChannel(String channelName);
    void deleteChannel(UUID channelId);
    UUID findChannelIdByName(String channelName);
    Channel findChannelById(UUID channelId);
}
