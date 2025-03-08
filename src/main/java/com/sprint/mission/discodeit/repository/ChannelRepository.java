package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    void save(Channel channel);
    Channel findById(UUID channelId);
    List<Channel> findAll();
    void deleteById(UUID channelId);
    boolean exists(UUID channelId);
}
