package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelService implements ChannelService {
    private final List<Channel> channelList;

    public JCFChannelService() {
        this.channelList = new ArrayList<>();
    }

    @Override
    public Channel create(Channel channel) {
        this.channelList.add(channel);
        return channel;
    }

    @Override
    public Channel findByChannelName(String channelName) {
        for (Channel channel : this.channelList) {
            if (channel.getChannelName().equals(channelName)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelList);
    }

    @Override
    public Channel update(String channelName, Channel channel) {
        for (int i = 0; i < channelList.size(); i++) {
            Channel channelForm = channelList.get(i);
            if (channelForm.getChannelName().equals(channelName)) {
                channelForm.setChannelName(channel.getChannelName());
                return channelForm;
            }
        }
        return null;
    }

    @Override
    public void delete(String channelName) {
        channelList.removeIf(channel -> channel.getChannelName().equals(channelName));
    }
}
