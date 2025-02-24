package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channels = new HashMap<>();

    public JCFChannelService() {}

    @Override
    public void create(Channel channel) {
        channels.put(channel.getId(), channel);
        System.out.println("[채널 ID] " + channel.getId());
    }

    @Override
    public Channel find(UUID id) {
        return channels.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void update(UUID id, Channel channel) {
        Channel c = channels.get(id);
        c.updateChannelName(c.getChannelName());
    }

    @Override
    public void delete(UUID id) {
        channels.remove(id);
    }
}