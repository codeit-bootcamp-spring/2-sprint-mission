package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel getChannel(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(UUID id, String newChannelName) {
        Channel channel = data.get(id);
        if (channel != null) {
            channel.updateChannelName(newChannelName);
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
    }
}
