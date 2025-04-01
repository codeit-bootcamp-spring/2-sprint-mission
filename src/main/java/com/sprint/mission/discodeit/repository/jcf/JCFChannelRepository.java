package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channelData;

    public JCFChannelRepository() {
        this.channelData = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        this.channelData.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(this.channelData.get(channelId));
    }

    @Override
    public List<Channel> findAll() {
        return this.channelData.values().stream().toList();
    }

    @Override
    public boolean existsById(UUID channelId) {
        return channelData.containsKey(channelId);
    }

    @Override
    public void deleteById(UUID channelId) {
        this.channelData.remove(channelId);
    }
}