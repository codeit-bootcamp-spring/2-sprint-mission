package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {

    private List<Channel> channelData;

    private JCFChannelRepository() {
        channelData = new ArrayList<>();
    }

    @Override
    public Channel save(Channel channel) {
        channelData.add(channel);
        return channel;
    }

    @Override
    public List<Channel> load() {
        return channelData.stream().toList();
    }

    @Override
    public void remove(Channel channel) {
        channelData.remove(channel);
    }

    @Override
    public Optional<Channel> loadToId(UUID uuid){
        return channelData.stream().filter(m -> m.getId().equals(uuid)).findFirst();
    }
}
