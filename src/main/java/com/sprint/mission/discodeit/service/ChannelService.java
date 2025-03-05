package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelType type, String channelName, String description);
    List<Channel> findAll();
    Channel findByChannelId(UUID channelId);
    Channel update(UUID channelId, String newChannelName, String newDescription);
    void delete(UUID channelId);
}
