package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    void delete(UUID channelKey);
    List<Channel> findAll();
    List<Channel> findAllByKeys(List<UUID> channelKeys);
    Channel findByKey(UUID channelKey);
    boolean existsByKey(UUID channelKey);
}
