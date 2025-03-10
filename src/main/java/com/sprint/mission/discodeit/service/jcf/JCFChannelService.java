package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channels = new HashMap<>();

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(UUID.randomUUID(), name, type, description);
        channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        return channels.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public Channel update(UUID channelId, String newName, ChannelType newType, String newDescription) {
        Channel channel = channels.get(channelId);
        if (channel != null) {
            channel.updateName(newName);
            channel.setType(newType);
            channel.setDescription(newDescription);
        }
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        channels.remove(channelId);
    }
}
