package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void create(Channel channel);
    Channel findById(UUID channelId);
    List<Channel> findAll();
    void delete(UUID id);
    void update(UUID id, String name);
}
