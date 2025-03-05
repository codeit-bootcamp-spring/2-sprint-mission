package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.lang.reflect.Array;
import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channels = new HashMap<>();

    @Override
    public void create(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    @Override
    public void update(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public Channel find(UUID id) {
        return channels.getOrDefault(id, null);
    }

    @Override
    public void delete(UUID id) {
        channels.remove(id);
    }
}
