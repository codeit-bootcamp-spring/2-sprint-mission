package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelStorage = new HashMap<>();

    @Override
    public void save(Channel channel) {
        channelStorage.put(channel.getId(), channel);
    }

    @Override
    public Channel findById(UUID id) {
        return channelStorage.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelStorage.values());
    }


    public void delete(UUID id) {
        channelStorage.remove(id);
    }
}
