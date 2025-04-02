package com.sprint.mission.discodeit.repository.jcf;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
@Repository
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channelList = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        channelList.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findChannelById(UUID channelUUID) {
        return Optional.of(channelList.get(channelUUID));
    }

    @Override
    public List<Channel> findAllChannel() {
        return channelList.values().stream().toList();
    }

    @Override
    public void delete(UUID channelUUID) {
        channelList.remove(channelUUID);
    }
}
