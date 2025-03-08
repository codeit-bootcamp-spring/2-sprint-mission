package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private static final Map<UUID, Channel> channels = new HashMap<>();

    @Override
    public void save(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    @Override
    public Channel findById(UUID channelId) {
        validateChannelExists(channelId);
        return channels.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void deleteById(UUID channelId) {
        validateChannelExists(channelId);
        channels.remove(channelId);
    }

    @Override
    public boolean exists(UUID channelId) {
        return channels.containsKey(channelId);
    }

    public void validateChannelExists(UUID channelId) {
        if(!channels.containsKey(channelId)){
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }
}
