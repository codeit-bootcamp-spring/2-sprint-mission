package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channels = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        channels.put(channel.getId(), channel);

        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channels.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return channels.values()
                .stream()
                .toList();
    }

    @Override
    public Channel updateName(UUID id, String name) {
        Channel channel = channels.get(id);
        channel.updateName(name);

        return channel;
    }

    @Override
    public void delete(UUID id) {
        channels.remove(id);
    }
}
