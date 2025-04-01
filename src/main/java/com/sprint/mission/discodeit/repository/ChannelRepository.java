package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);

    List<Channel> findAll();

    Channel findById(UUID channelId);

    void delete(UUID channelId);

    Map<UUID, Channel> getChannelData();

    Channel update(Channel channel, String newName, String newDescription);
}
