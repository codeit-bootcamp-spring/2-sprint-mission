package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService(Map<UUID, Channel> channels) {
        this.data = new HashMap<>(channels);
    }

    @Override
    public Channel createChannel(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel readChannel(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> readAllChannels() {
        return List.of();
    }

    @Override
    public Channel updateChannel(UUID id, Channel channel) {
        if(data.containsKey(id)) {
            Channel existingChannel = data.get(id);
            existingChannel.update(channel.getChannelName());
            return existingChannel;
        }
        return null;
    }

    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
    }
}
