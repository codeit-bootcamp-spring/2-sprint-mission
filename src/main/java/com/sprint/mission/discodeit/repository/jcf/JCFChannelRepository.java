package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.List;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getUuid(), channel);

        return data.get(channel.getUuid());
    }

    @Override
    public void delete(UUID channelKey) {
        data.remove(channelKey);
    }

    @Override
    public Channel findByKey(UUID channelKey) {
        return data.get(channelKey);
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public List<Channel> findAllByKeys(List<UUID> channelKeys) {
        return channelKeys.stream()
                .map(data::get)
                .toList();
    }

    @Override
    public boolean existsByKey(UUID channelKey) {
        return data.containsKey(channelKey);
    }
}
