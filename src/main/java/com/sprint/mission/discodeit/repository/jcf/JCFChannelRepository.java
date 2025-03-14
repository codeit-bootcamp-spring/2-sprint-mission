package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelRepository implements ChannelRepository {

    private List<Channel> channelData;

    private JCFChannelRepository() {
        channelData = new ArrayList<>();
    }

    @Override
    public void save(Channel channel) {
        channelData.add(channel);
    }

    @Override
    public List<Channel> load() {
        return channelData.stream().toList();
    }

    @Override
    public void remove(Channel channel) {
        channelData.remove(channel);
    }
}
