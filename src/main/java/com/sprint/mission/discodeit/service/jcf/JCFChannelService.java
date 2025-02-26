package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private static final JCFChannelService instance = new JCFChannelService();
    private final Map<UUID, Channel> data = new HashMap<>();

    private JCFChannelService() {}

    public static JCFChannelService getInstance() {
        return instance;
    }

    @Override
    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> getChannelById(UUID channelId) {
        return Optional.ofNullable(data.get(channelId));
    }

    @Override
    public List<Channel> getChannelsByName(String name) {
        return data.values().stream()
                .filter(channel -> channel.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(UUID channelId, String newName) {
        Channel channel = data.get(channelId);
        if (channel != null) {
            channel.update(newName);
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        data.remove(channelId);
    }
}
