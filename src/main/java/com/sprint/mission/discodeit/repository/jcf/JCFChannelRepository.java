package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private static final Map<UUID, Channel> channels = new HashMap<>();

    @Override
    public void save() {}

    @Override
    public void addChannel(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        return channels.get(channelId);
    }

    @Override
    public List<Channel> findAllChannels() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void deleteChannelById(UUID channelId) {
        channels.remove(channelId);
    }

    @Override
    public boolean existsById(UUID channelId) {
        return channels.containsKey(channelId);
    }
}
