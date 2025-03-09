package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    void delete(UUID channelKey);
    Channel findByKey(UUID channelKey);
    List<Channel> findAllByKeys(List<UUID> channelKeys);
    boolean existsByKey(UUID channelKeys);
    UUID findKeyByName(String name);
    List<UUID> findKeyByNames(List<String> names);
}
