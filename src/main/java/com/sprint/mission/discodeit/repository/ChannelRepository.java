package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Channel findById(UUID id);
    List<Channel> findAll();
    Channel update(UUID id, String newName, ChannelType channelType);
    void delete(UUID id);
}
