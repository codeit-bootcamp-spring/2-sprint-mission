package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channelData;

    public JCFChannelRepository() {
        this.channelData = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        this.channelData.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(this.channelData.get(channelId));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelData.values());
    }

    @Override
    public boolean existsById(UUID channelId) {
        return channelData.containsKey(channelId);
    }

    @Override
    public void delete(UUID channelId) {
        this.channelData.remove(channelId);
    }
}