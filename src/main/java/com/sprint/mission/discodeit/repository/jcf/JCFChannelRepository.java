package com.sprint.mission.discodeit.repository.jcf;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    List<Channel> channelList = new ArrayList<>();

    @Override
    public Channel channelSave(String channelName) {
        Channel channel = new Channel(channelName);
        channelList.add(channel);
        return channel;
    }

    @Override
    public Optional<Channel> findChannelById(UUID channelUUID) {
        return channelList.stream()
                .filter(channel -> channel.getId().equals(channelUUID))
                .findAny();
    }

    @Override
    public List<Channel> findAllChannel() {
        return channelList;
    }

    @Override
    public Channel updateChannelChannelName(UUID channelUUID, String channelName) {
        return channelList.stream()
                .filter(channel -> channel.getId().equals(channelUUID))
                .findAny()
                .map(channel -> {
                    channel.updateChannelName(channelName);
                    return channel;
                })
                .orElse(null);
    }

    @Override
    public boolean deleteChannelById(UUID channelUUID) {
        return channelList.removeIf(channel -> channel.getId().equals(channelUUID));
    }
}
