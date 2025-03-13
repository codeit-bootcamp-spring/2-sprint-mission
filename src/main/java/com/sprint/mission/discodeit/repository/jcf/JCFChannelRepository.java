package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

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
        return new ArrayList<>(this.channelMap.values());
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(this.channelMap.get(channelId));
    }

    @Override
    public Channel update(Channel channel) {
        channelMap.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public boolean delete(UUID channelId) {
        return this.channelMap.remove(channelId) != null;
    }
}
