package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data;

    public JCFChannelRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        this.data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        Channel channelNullable = this.data.get(channelId);
        return Optional.ofNullable(channelNullable)
                .orElseThrow(()-> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(this.data.values());
    }

    @Override
    public Channel update(Channel channel) {
        if(!this.data.containsKey(channel.getId())) {
            throw new NoSuchElementException("Channel with id " + channel.getId() + " not found");
        }
        this.data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if(!this.data.containsKey(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        this.data.remove(channelId);
    }

    @Override
    public boolean exists(UUID channelId) {
        return this.data.containsKey(channelId);
    }

    @Override
    public boolean existsById(UUID channelId) {
        return this.data.containsKey(channelId);
    }
}
