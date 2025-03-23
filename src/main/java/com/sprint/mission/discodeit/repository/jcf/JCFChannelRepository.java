package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelMap;

    public JCFChannelRepository() {
        this.channelMap = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        this.channelMap.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return this.channelMap.values().stream().toList();
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(this.channelMap.get(channelId));
    }

    @Override
    public boolean existsById(UUID channelId) {
        return this.channelMap.containsKey(channelId);
    }

    @Override
    public void deleteById(UUID channelId) {
        this.channelMap.remove(channelId);
    }
}
