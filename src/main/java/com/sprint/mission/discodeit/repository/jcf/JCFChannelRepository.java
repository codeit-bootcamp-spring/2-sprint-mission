package com.sprint.mission.discodeit.repository.jcf;


import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
public class JCFChannelRepository implements ChannelRepository {

    private final List<Channel> channelList = new ArrayList<>();

    @Override
    public Channel save(Channel channel) {
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
