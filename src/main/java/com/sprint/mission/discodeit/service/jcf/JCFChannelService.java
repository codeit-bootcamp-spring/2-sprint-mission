package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel create(String name, String topic) {
        Channel channel = new Channel(name, topic);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        return data.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID channelId, String name, String topic) {
        Channel channel = data.get(channelId);
        if (channel != null) {
            channel.update(name, topic);
        }
        return channel;
    }

    @Override
    public void deleteById(UUID channelId) {
        data.remove(channelId);
    }
}
