package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(String name, String topic);
    Channel findById(UUID channelId);
    List<Channel> findAll();
    Channel update(UUID channelId, String name, String topic);
    void deleteById(UUID channelId);
}
