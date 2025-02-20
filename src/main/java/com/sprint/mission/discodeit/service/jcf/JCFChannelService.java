package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> getChannelById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<Channel> getChannelByName(String name) {
        return data.values().stream()
                .filter(channel -> channel.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<Channel> getAllChannels() {
        return data.values().stream().toList();
    }

    @Override
    public void updateChannelName(UUID id, String name) {
        if (data.containsKey(id)) {
            data.get(id).updateName(name);
        }
    }

    @Override
    public void deleteChannel(UUID id) {
            data.remove(id);
    }
}
