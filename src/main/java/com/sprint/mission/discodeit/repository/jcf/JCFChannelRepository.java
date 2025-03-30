package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final HashMap<UUID, Channel> channels = new HashMap<>();

    @Override
    public void save(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channels.get(id));
    }

    @Override
    public Optional<List<Channel>> findAll() {
        return Optional.of(new ArrayList<>(channels.values()));
    }

    @Override
    public void update(Channel channel) {
       save(channel);
    }

    @Override
    public void delete(UUID id) {
        channels.remove(id);
    }
}
