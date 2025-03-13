package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

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
    public boolean existsByKey(UUID channelKey) {
        return data.containsKey(channelKey);
    }

    @Override
    public Channel findByName(String name) {
        return data.values().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Channel> findAllByNames(List<String> names) {
        return data.values().stream()
                .filter(c -> names.contains(c.getName()))
                .toList();
    }
}
