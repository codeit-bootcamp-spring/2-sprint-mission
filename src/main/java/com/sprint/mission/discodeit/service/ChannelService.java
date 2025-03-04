package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(ChannelType type, String channelName, String description);
    List<Channel> findAllChannel();
    Channel findByChannelId(UUID channelId);
    Channel updateChannel(UUID channelId, String newChannelName, String newDescription);
    void deleteChannel(UUID channelId);
}
