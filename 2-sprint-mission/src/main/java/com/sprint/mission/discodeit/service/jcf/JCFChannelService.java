package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        data = new HashMap<UUID, Channel>();
    }

    @Override
    public void createChannel(String channelname) {
        Channel channel = new Channel(channelname);
        data.put(channel.getId(), channel);
    }

    @Override
    public Channel getChannelById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> getAllChannels(){
        return new ArrayList<Channel>(data.values());
    }

    @Override
    public Channel updateChannelName(UUID id, String channelname) {
        Channel channel = data.get(id).updateChannel(channelname);
        data.put(id, channel);
        return channel;
    }

    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
    }
}
