package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    public void updateName(UUID id, String name) {
        Channel channel = channels.get(id);
        channel.updateName(name);
    }

    @Override
    public void delete(UUID id) {
        channels.remove(id);
    }
}
